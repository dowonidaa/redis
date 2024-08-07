package com.example.redis.practice_5_my;

import com.example.redis.practice_5_my.dto.StoreConverter;
import com.example.redis.practice_5_my.dto.StoreDto;
import com.example.redis.practice_5_my.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;

    @CachePut(cacheNames = "store", key = "#result.id")
    public StoreDto create(StoreDto storeDto) {
        Store store = StoreConverter.convertToEntity(storeDto);
        storeRepository.save(store);
        return storeDto;
    }

    @Cacheable(cacheNames = "store", key = "args[0]")
    public StoreDto readOne(Long id) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return StoreConverter.convertToDto(store);

    }

    @Cacheable(cacheNames = "storeAll", key = "methodName")
    public List<StoreDto> readAll() {
        return storeRepository.findAll()
                .stream()
                .map(StoreConverter::convertToDto)
                .toList();
    }


    @CachePut(cacheNames = "store", key = "args[0]")
    @CacheEvict(cacheNames = "storeAll", allEntries = true)
    public StoreDto update(Long id, StoreDto dto) {
        Store store = storeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        store.update(dto);
        return StoreConverter.convertToDto(store);
    }

    @CacheEvict(cacheNames = {"storeAll","store"}, key = "args[0]")
    public void delete(Long id) {
        storeRepository.deleteById(id);
    }

}
