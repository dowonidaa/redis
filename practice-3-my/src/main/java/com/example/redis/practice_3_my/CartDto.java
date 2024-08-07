package com.example.redis.practice_3_my;

import lombok.*;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class CartDto {

    private Set<CartItemDto> items;
    private Date expireAt;

    public static CartDto fromHashPairs(Map<String, Integer> entries, Date expireAt) {
        return CartDto.builder()
                .items(entries.entrySet().stream()
                        .map(entry -> CartItemDto.builder()
                                .item(entry.getKey())
                                .quantity(entry.getValue())
                                .build())
                        .collect(Collectors.toUnmodifiableSet()))
                .expireAt(expireAt)
                .build();
    }
}
