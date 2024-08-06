package com.example.redis;

import com.example.redis.repository.Item;
import com.example.redis.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void createTest() {
        Item item = Item.builder()
                .name("keyboard")
                .description("Very Expensive Keyboard")
                .price(100000)
                .build();
        itemRepository.save(item);
    }

    @Test
    void readOneTest() {
        Item item = itemRepository.findById("bd883c35-d80f-49ef-bec8-ba041a2d1af9").orElseThrow();
        System.out.println(item.getDescription());
    }
    
    @Test
    void updateTest() {
        Item item = itemRepository.findById("bd883c35-d80f-49ef-bec8-ba041a2d1af9").orElseThrow();

        item.setDescription("On Sale!!");
        item = itemRepository.save(item);
        System.out.println("item.getDescription() = " + item.getDescription());
    }

    @Test
    void deleteTest() {
        itemRepository.deleteById("bd883c35-d80f-49ef-bec8-ba041a2d1af9");

    }

    @Test
    void deleteAll() {
        itemRepository.deleteAll();
    }
}
