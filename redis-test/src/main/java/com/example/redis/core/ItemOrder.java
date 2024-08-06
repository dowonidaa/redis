package com.example.redis.core;

import com.example.redis.ItemOrderDto;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("order")
@Builder
public class ItemOrder {

    @Id
    private String id;
    private String name;
    private Integer quantity;
    private Long totalPrice;
    private String status;

    public ItemOrder(ItemOrderDto dto) {
        this.name = dto.getName();
        this.quantity = dto.getQuantity();
        this.totalPrice = dto.getPrice() * dto.getQuantity();
        this.status = dto.getStatus();

    }

    public void update(ItemOrderDto dto) {
        this.name = dto.getName();
        this.quantity = dto.getQuantity();
        this.totalPrice = dto.getPrice() * dto.getQuantity();
        this.status = dto.getStatus();
    }
}
