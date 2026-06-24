package com.shopsphere.order_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NotNull(message = "User Id is required")
    private Long userId;

    @NotNull(message = "Product Id is required")
    private Long productId;

    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;

    @Positive(message = "Total Price must be greater than 0")
    private Double totalPrice;
}