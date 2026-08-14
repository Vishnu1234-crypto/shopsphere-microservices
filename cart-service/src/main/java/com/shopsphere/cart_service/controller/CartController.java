package com.shopsphere.cart_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopsphere.cart_service.dto.CartRequest;
import com.shopsphere.cart_service.dto.CartResponse;
import com.shopsphere.cart_service.dto.OrderResponse;
import com.shopsphere.cart_service.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartResponse> addToCart(
           @Valid @RequestBody CartRequest request) {

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
    @PostMapping("/{userId}/checkout")
    public ResponseEntity<OrderResponse> checkout(
            @PathVariable Long userId,
            @RequestParam String upiId) {

        return ResponseEntity.ok(
                cartService.checkout(userId, upiId)
        );
    }
}