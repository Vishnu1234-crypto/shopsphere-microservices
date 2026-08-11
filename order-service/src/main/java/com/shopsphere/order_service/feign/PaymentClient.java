package com.shopsphere.order_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.shopsphere.order_service.dto.PaymentRequest;

@FeignClient(name = "PAYMENT-SERVICE")
public interface PaymentClient {

	@PostMapping("/api/payments")
	Object createPayment(@RequestBody PaymentRequest request);
}