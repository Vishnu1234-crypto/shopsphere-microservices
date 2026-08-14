package com.shopsphere.order_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private Long id;

    private Long productId;

    private Integer quantity;

    private Double price;

    private Double subtotal;
}