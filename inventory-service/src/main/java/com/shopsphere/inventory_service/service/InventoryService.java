package com.shopsphere.inventory_service.service;

import java.util.List;

import com.shopsphere.inventory_service.dto.InventoryRequest;
import com.shopsphere.inventory_service.dto.InventoryResponse;

public interface InventoryService {

    InventoryResponse createInventory(
            InventoryRequest request
    );

    List<InventoryResponse> getAllInventory();

    InventoryResponse getInventoryByProductId(
            Long productId
    );

    InventoryResponse updateInventory(
            Long productId,
            InventoryRequest request
    );

    InventoryResponse increaseStock(
            Long productId,
            Integer quantity
    );

    InventoryResponse reduceStock(
            Long productId,
            Integer quantity
    );

    InventoryResponse restoreStock(
            Long productId,
            Integer quantity
    );

    void deleteInventory(
            Long productId
    );
}