package com.example.redis.practice_5_my.dto;

import com.example.redis.practice_5_my.entity.Store;

public class StoreConverter {

    public static StoreDto convertToDto(Store store) {
      return StoreDto.builder().id(store.getId()).name(store.getName()).category(store.getCategory()).build();
    }

    public static Store convertToEntity(StoreDto store) {
        return Store.builder().name(store.getName()).category(store.getCategory()).build();
    }
}
