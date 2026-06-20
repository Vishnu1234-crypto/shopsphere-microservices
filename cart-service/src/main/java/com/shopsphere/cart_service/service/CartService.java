package com.shopsphere.cart_service.service;

import com.shopsphere.cart_service.dto.CartRequest;
import com.shopsphere.cart_service.dto.CartResponse;

import java.util.List;

public interface CartService {

    CartResponse addToCart(CartRequest request);

    List<CartResponse> getCartByUser(Long userId);

    void removeFromCart(Long cartId);
}