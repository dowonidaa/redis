package com.example.redis;

import com.example.redis.core.ItemOrder;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class ItemOrderController {

    private final ItemOrderRepository itemOrderRepository;

    @PostMapping
    public ItemOrder createItemOrder(@RequestBody ItemOrderDto dto) {
        return itemOrderRepository.save(new ItemOrder(dto));
    }

    @GetMapping
    public List<ItemOrder> getOrders() {
        List<ItemOrder> orders = new ArrayList<>();
        itemOrderRepository.findAll().forEach(orders::add);
        return orders;
    }

    @GetMapping("/{id}")
    public ItemOrder getOrderById(@PathVariable String id) {
        return itemOrderRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("주문을 찾을수 없습니다."));
    }

    @PutMapping("/{id}")
    public ItemOrder updateOrder(@PathVariable String id, @RequestBody ItemOrderDto dto) {
        ItemOrder itemOrder = itemOrderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("주문을 찾을수 없습니다."));
        itemOrder.update(dto);
        return itemOrderRepository.save(itemOrder);
    }

    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable String id) {
        itemOrderRepository.deleteById(id);
        return "delete Ok :" + id;
    }
}
