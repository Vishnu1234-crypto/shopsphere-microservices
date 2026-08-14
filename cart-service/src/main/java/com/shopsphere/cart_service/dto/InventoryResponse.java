package com.shopsphere.cart_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {

    private Long id;

    private Long productId;

    private Integer quantity;

    private Integer reservedQuantity;

    private Integer availableQuantity;
}