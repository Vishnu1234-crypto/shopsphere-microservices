package com.shopsphere.cart_service.service;

import java.util.List;

import com.shopsphere.cart_service.dto.CartRequest;
import com.shopsphere.cart_service.dto.CartResponse;
import com.shopsphere.cart_service.dto.OrderResponse;
public interface CartService {

    CartResponse addToCart(CartRequest request);

    List<CartResponse> getCartByUser(Long userId);

    void removeFromCart(Long cartId);

    OrderResponse checkout(Long userId, String upiId);
}