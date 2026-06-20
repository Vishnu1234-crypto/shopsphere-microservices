package com.shopsphere.inventory_service.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {

    private Long productId;

    private Integer quantity;
}