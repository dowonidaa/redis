package com.example.redis;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemOrderDto {

    private String name;
    private Integer quantity;
    private Long price;
    private String status;
}
