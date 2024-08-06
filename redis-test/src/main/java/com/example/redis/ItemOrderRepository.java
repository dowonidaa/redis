package com.example.redis;

import com.example.redis.core.ItemOrder;
import org.springframework.data.repository.CrudRepository;

public interface ItemOrderRepository extends CrudRepository<ItemOrder, String> {
}
