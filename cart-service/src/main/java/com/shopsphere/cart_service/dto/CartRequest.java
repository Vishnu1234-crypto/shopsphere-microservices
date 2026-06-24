package com.shopsphere.cart_service.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequest {
	@NotNull
	private Long userId;

	@NotNull
	private Long productId;

	@Positive
	private Integer quantity;
}