package com.shopsphere.payment_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class PaymentRequest {

	@NotNull(message = "Order Id is required")
	private Long orderId;

	@Positive(message = "Amount must be greater than 0")
	private Double amount;
    private String paymentMethod;
}