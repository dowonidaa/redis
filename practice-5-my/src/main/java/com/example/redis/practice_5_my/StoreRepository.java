package com.example.redis.practice_5_my;

import com.example.redis.practice_5_my.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
