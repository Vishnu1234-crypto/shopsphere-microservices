package com.shopsphere.order_service.dto;

import lombok.Data;

@Data
public class InventoryResponse {

	private Long id;
	private Long productId;
	private Integer quantity;
}