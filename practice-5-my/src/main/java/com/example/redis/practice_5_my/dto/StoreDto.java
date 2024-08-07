package com.example.redis.practice_5_my.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDto {

    private Long id;
    private String name;
    private String category;


}
