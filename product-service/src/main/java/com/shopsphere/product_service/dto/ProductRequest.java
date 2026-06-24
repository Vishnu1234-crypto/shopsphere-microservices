package com.shopsphere.product_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class ProductRequest {

	@NotBlank
	private String name;

	@Positive
	private Double price;

	@PositiveOrZero
	private Integer stock;

    private String description;

    
}