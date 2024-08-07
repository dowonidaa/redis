package com.example.redis.practice_5_my;

import com.example.redis.practice_5_my.dto.StoreConverter;
import com.example.redis.practice_5_my.dto.StoreDto;
import com.example.redis.practice_5_my.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    // 새로 만든 상점은 다음 검색에서 등장할 수 있게 기존의 전체 조회 캐시 삭제
    // 인지도가 높지 않을 가능성이 있으므로 생성시 추가 캐시 생성 X
    @CacheEvict(cacheNames = "storeAllCache", allEntries = true)
    public StoreDto create(StoreDto storeDto) {
        return StoreConverter.convertToDto(storeRepository.save(StoreConverter.convertToEntity(storeDto)));
    }

    @Cacheable(cacheNames = "storeCache", key = "args[0]")
    public StoreDto readOne(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return StoreConverter.convertToDto(store);

    }

    @Cacheable(cacheNames = "storeAllCache", key = "methodName")
    public List<StoreDto> readAll() {
        return storeRepository.findAll()
                .stream()
                .map(StoreConverter::convertToDto)
                .toList();
    }


    @CachePut(cacheNames = "storeCache", key = "args[0]")
    @CacheEvict(cacheNames = "storeAllCache", allEntries = true)
    public StoreDto update(Long id, StoreDto dto) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        store.update(dto);
        return StoreConverter.convertToDto(store);
    }


    @Caching(
            evict = {
                    @CacheEvict(cacheNames = "storeCache", key = "args[0]"),
                    @CacheEvict(cacheNames = "storeAllCache", allEntries = true)
            }) //여러 조건 넣을때 유용
//    @CacheEvict(cacheNames = {"storeAllCache","storeCache"}, key = "args[0]")
    public void delete(Long id) {
        storeRepository.deleteById(id);
    }

}
