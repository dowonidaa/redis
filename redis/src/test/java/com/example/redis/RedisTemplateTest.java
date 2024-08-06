package com.example.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

@SpringBootTest
public class RedisTemplateTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void stringOpsTest() {
        // 문자열 조작을 위한 클래스
        ValueOperations<String, String> ops
                // 지금 RedisTemplate에 설정된 타입을 바탕으로
                // Redis 문자열 조작을 할거다.
                = stringRedisTemplate.opsForValue();
        ops.set("simpleKey", "simpleValue");
        System.out.println(ops.get("simpleKey"));

        // 집합을 조작하기 위한 클래스
        SetOperations<String, String> setOps
                = stringRedisTemplate.opsForSet();
        setOps.add("hobbies", "games");
        setOps.add("hobbies",
                "coding","alcohol","games");
        System.out.println(setOps.size("hobbies"));
//        System.out.println(setOps.pop("hobbies", 10));

        stringRedisTemplate.expire("hobbies", 10, TimeUnit.SECONDS);
        stringRedisTemplate.delete("simpleKey");
    }

    @Autowired
    private RedisTemplate<String, ItemDto> itemRedisTemplate;

    @Test
    void itemRedisTemplateTest() {
        ValueOperations<String, ItemDto> ops = itemRedisTemplate.opsForValue();
        ops.set("my:Keyboard", ItemDto.builder()
                .name("Mechanical Keyboard")
                .price(250000)
                .description("OMG")
                .build());
        System.out.println("ops = " + ops.get("my:Keyboard"));
        
        ops.set("my:mouse", ItemDto.builder()
                .name("maous")
                .price(10000)
                .description("So Cute")
                .build());
        System.out.println("ops.get(\"my:mouse\") = " + ops.get("my:mouse"));
    }

}
