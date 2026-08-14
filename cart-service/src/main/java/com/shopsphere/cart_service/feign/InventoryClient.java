package com.shopsphere.cart_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shopsphere.cart_service.dto.InventoryResponse;

@FeignClient(name = "INVENTORY-SERVICE")
public interface InventoryClient {

    @GetMapping("/api/inventory/{productId}")
    InventoryResponse getInventory(
            @PathVariable Long productId
    );

    @PutMapping("/api/inventory/{productId}/reduce")
    InventoryResponse reduceStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity
    );

    @PutMapping("/api/inventory/{productId}/restore")
    InventoryResponse restoreStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity
    );
}