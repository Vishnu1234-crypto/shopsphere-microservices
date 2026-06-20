package com.shopsphere.cart_service.service.impl;

import com.shopsphere.cart_service.dto.CartRequest;
import com.shopsphere.cart_service.dto.CartResponse;
import com.shopsphere.cart_service.entity.Cart;
import com.shopsphere.cart_service.repository.CartRepository;
import com.shopsphere.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    @Override
    public CartResponse addToCart(CartRequest request) {

        Cart cart = Cart.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .build();

        cart = cartRepository.save(cart);

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .productId(cart.getProductId())
                .quantity(cart.getQuantity())
                .build();
    }

    @Override
    public List<CartResponse> getCartByUser(Long userId) {

        return cartRepository.findByUserId(userId)
                .stream()
                .map(cart -> CartResponse.builder()
                        .id(cart.getId())
                        .userId(cart.getUserId())
                        .productId(cart.getProductId())
                        .quantity(cart.getQuantity())
                        .build())
                .toList();
    }

    @Override
    public void removeFromCart(Long cartId) {
        cartRepository.deleteById(cartId);
    }
}