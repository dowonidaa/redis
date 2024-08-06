package com.example.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articles")
public class RedisController {

    private final ValueOperations<String, Integer> ops;

    public RedisController(RedisTemplate<String,Integer> articleTemplate) {
        this.ops = articleTemplate.opsForValue();
    }

    @GetMapping("/{id}")
    public Long views(@PathVariable Long id) {
        return ops.increment("articles:%d".formatted(id));
    }


}
