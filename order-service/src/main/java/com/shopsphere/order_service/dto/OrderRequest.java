package com.shopsphere.order_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
public class OrderRequest {

    @NotNull(message = "User Id is required")
    private Long userId;

    @NotNull(message = "Product Id is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater than 0")
    private Integer quantity;

    @NotNull(message = "Total Price is required")
    @Positive(message = "Total Price must be greater than 0")
    private Double totalPrice;

    @NotBlank(message = "UPI ID is required")
    private String upiId;
}