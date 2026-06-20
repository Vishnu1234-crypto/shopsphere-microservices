package com.shopsphere.inventory_service.service;

import com.shopsphere.inventory_service.dto.InventoryRequest;
import com.shopsphere.inventory_service.dto.InventoryResponse;

public interface InventoryService {

    InventoryResponse createInventory(InventoryRequest request);

    InventoryResponse getInventory(Long productId);

    InventoryResponse addStock(Long productId, Integer quantity);

    InventoryResponse reduceStock(Long productId, Integer quantity);
}