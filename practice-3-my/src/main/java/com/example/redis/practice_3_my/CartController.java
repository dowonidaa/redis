package com.example.redis.practice_3_my;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public CartDto modifyCart(@RequestBody CartItemDto cartItemDto, HttpSession session) {
        cartService.modifyCart(session.getId(), cartItemDto);
        return cartService.getCart(session.getId());
    }

    @GetMapping
    public CartDto getCart(HttpSession session) {
        return cartService.getCart(session.getId());
    }
}
