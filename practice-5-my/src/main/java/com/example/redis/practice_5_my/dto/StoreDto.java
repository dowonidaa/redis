package com.example.redis.practice_5_my.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StoreDto implements Serializable {

    private Long id;
    private String name;
    private String category;


}
