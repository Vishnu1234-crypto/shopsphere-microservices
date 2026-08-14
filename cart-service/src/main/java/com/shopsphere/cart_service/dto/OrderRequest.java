package com.shopsphere.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private Long userId;

    private Long productId;

    private Integer quantity;

    private Double totalPrice;

    private String upiId;
}