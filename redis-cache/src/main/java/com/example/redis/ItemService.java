package com.example.redis;

import com.example.redis.domain.Item;
import com.example.redis.domain.ItemDto;
import com.example.redis.domain.ItemOrder;
import com.example.redis.domain.ItemOrderDto;
import com.example.redis.repo.ItemRepository;
import com.example.redis.repo.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;
    private final ZSetOperations<String, ItemDto> rankOps;
    private final RedisTemplate<String, ItemOrderDto> orderTemplate;
    private final ListOperations<String, ItemOrderDto> orderOps;
    private final ReactiveRedisTemplate reactiveRedisTemplate;

    public ItemService(ItemRepository itemRepository,
                       OrderRepository orderRepository,
                       RedisTemplate<String, ItemDto> rankTemplate,
                       RedisTemplate<String, ItemOrderDto> orderTemplate,
                       @Qualifier("reactiveRedisTemplate") ReactiveRedisTemplate reactiveRedisTemplate) {
        this.itemRepository = itemRepository;
        this.orderRepository = orderRepository;
        this.rankOps = rankTemplate.opsForZSet();
        this.orderTemplate = orderTemplate;
        this.orderOps = this.orderTemplate.opsForList();
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }


    @CachePut(cacheNames = "itemCache", key = "#result.id")
    public ItemDto create(ItemDto dto) {
        return ItemDto.fromEntity(itemRepository.save(Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build()));
    }


    //이 메서드의 결과는 캐싱이 가능하다
    // cacheNames: 적용할 캐시 규칙을 지정하기 위한 이름
    // cacheNames: 이 메서드로 인해서 만들어질 캐시를 지칭하는 이름
    // key: 캐시 데이터를 구분하기 위해 활용하는 값
    @Cacheable(cacheNames = "itemCache", key = "args[0]")
    public ItemDto readOne(Long id) {
        log.info("Read One: {}", id);
        return itemRepository.findById(id)
                .map(ItemDto::fromEntity)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Cacheable(cacheNames = "itemAllCache", key = "methodName")
    public List<ItemDto> readAll() {
        return itemRepository.findAll()
                .stream()
                .map(ItemDto::fromEntity)
                .toList();
    }

    @CachePut(cacheNames = "itemCache", key = "args[0]")
    @CacheEvict(cacheNames = "itemAllCache", allEntries = true)
//    @CacheEvict(cacheNames = "itemAllCache", key = "'readAll'")
    public ItemDto update(Long id, ItemDto dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setPrice(dto.getPrice());
        return ItemDto.fromEntity(itemRepository.save(item));
    }

    @CacheEvict(cacheNames = {"itemAllCache","itemCache"}, key = "args[0]")
    public void delete(Long id) {
        itemRepository.deleteById(id);
    }

    @Cacheable(cacheNames = "itemSearchCache", key = "{args[0], args[1].pageNumber, args[1].pageSize}")
    public Page<ItemDto> searchByName(String query, Pageable pageable) {
        return itemRepository.findAllByNameContains(query, pageable).map(ItemDto::fromEntity);
    }

    public void purchase(ItemOrderDto dto) {
        Item item = itemRepository.findById(dto.getItemId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
//        orderRepository.save(ItemOrder.builder()
//                .item(item)
//                .count(1)
//                .build());

        orderOps.rightPush("orderCache::behind", dto);
        rankOps.incrementScore(
                "soldRanks",
                ItemDto.fromEntity(item),
                1
        );
    }

    @Transactional
    @Scheduled(fixedRate = 20, timeUnit = TimeUnit.SECONDS)
    public void insertOrders() {
        boolean exists = Optional.ofNullable(orderTemplate.hasKey("orderCache::behind")).orElse(false);

        if (!exists) {
            log.info("no exists Cache");
            return;
        }
        orderTemplate.rename("orderCache::behind", "orderCache::now"); // 데이터 끊어주는 용도 이름변경하면 그 뒤로 들어오는 주문은 다시 orderCache::behind로 들어감
        orderRepository.saveAll(orderOps.range("orderCache::now", 0,-1).stream()
                .map(dto -> ItemOrder.builder()
                        .itemId(dto.getItemId())
                        .count(dto.getCount())
                        .build())
                .toList());
        orderTemplate.delete("orderCache::now");

    }

    public List<ItemDto> getMostSold() {
        Set<ItemDto> ranks = rankOps.reverseRange("soldRanks", 0, 9);
        if (ranks == null) return Collections.emptyList();
        return ranks.stream().toList();
    }


}
