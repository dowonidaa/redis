package com.example.redis.practice_3_my;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {

    private String item;
    private Integer quantity;
}
