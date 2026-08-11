package com.shopsphere.order_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopsphere.order_service.dto.InventoryResponse;

@FeignClient(name = "INVENTORY-SERVICE")
public interface InventoryClient {

	@GetMapping("/api/inventory/{id}")
	InventoryResponse getInventory(@PathVariable Long id);
}