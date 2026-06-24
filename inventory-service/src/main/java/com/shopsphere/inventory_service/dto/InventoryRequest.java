package com.shopsphere.inventory_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryRequest {
	@NotNull(message = "Product Id is required")
	private Long productId;

	@PositiveOrZero(message = "Quantity cannot be negative")
	private Integer quantity;
}