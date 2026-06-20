package com.shopsphere.order_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;

    private Long userId;

    private Long productId;

    private Integer quantity;

    private Double totalPrice;

    private String status;
}