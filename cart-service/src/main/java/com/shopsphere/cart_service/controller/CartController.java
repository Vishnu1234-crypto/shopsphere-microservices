package com.shopsphere.cart_service.controller;

import com.shopsphere.cart_service.dto.CartRequest;
import com.shopsphere.cart_service.dto.CartResponse;
import com.shopsphere.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> addToCart(
            @RequestBody CartRequest request) {

        return ResponseEntity.ok(
                cartService.addToCart(request)
        );
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<CartResponse>> getCartByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                cartService.getCartByUser(userId)
        );
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<String> removeFromCart(
            @PathVariable Long cartId) {

        cartService.removeFromCart(cartId);

        return ResponseEntity.ok(
                "Item removed from cart"
        );
    }
}