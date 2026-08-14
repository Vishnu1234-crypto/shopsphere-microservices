package com.shopsphere.order_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequest {

    @NotNull(message = "Product Id is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;
}