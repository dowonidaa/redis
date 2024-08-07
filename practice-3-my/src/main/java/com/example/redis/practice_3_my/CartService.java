package com.example.redis.practice_3_my;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class CartService {

    private final RedisTemplate<String, String> cartTemplate;
    private final HashOperations<String, String, Integer> hashOps;

    public CartService(RedisTemplate<String, String> cartTemplate) {
        this.cartTemplate = cartTemplate;
        this.hashOps = this.cartTemplate.opsForHash();
    }


    public void modifyCart(String id, CartItemDto cartItemDto) {
        hashOps.increment("cart:" + id, cartItemDto.getItem(), cartItemDto.getQuantity());
        int quantity = Optional.ofNullable(hashOps.get("cart:" + id, cartItemDto.getItem())).orElse(0);

        if (quantity <= 0) {
            hashOps.delete("cart:"+id, cartItemDto.getItem());
        }
    }

    public CartDto getCart(String id) {
        boolean exists = Optional.ofNullable(cartTemplate.hasKey("cart:" + id)).orElse(false);
        if (!exists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        Date expireAt = Date.from(Instant.now().plus(30, ChronoUnit.SECONDS));
        cartTemplate.expireAt("cart:"+id, expireAt);

        return CartDto.fromHashPairs(hashOps.entries("cart:"+id), expireAt);

    }
}
