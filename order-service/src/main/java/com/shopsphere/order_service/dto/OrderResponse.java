package com.shopsphere.order_service.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long id;

    private Long userId;
    
    private Long productId;

    private Double totalPrice;

    private String status;
    
    private int quantity;

    private List<OrderItemResponse> items;
}