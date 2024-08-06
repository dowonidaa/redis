package com.example.redis;

import lombok.*;

import java.io.Serializable;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto implements Serializable {
    private String name;
    private String description;
    private Integer price;
}
