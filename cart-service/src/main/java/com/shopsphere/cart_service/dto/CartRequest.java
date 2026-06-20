package com.shopsphere.cart_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {

    private Long userId;

    private Long productId;

    private Integer quantity;
}