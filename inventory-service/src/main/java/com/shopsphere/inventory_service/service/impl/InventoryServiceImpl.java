package com.shopsphere.inventory_service.service.impl;

import com.shopsphere.inventory_service.dto.InventoryRequest;
import com.shopsphere.inventory_service.dto.InventoryResponse;
import com.shopsphere.inventory_service.entity.Inventory;
import com.shopsphere.inventory_service.repository.InventoryRepository;
import com.shopsphere.inventory_service.service.InventoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public InventoryResponse createInventory(
            InventoryRequest request) {

        Inventory inventory = Inventory.builder()
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .build();

        inventoryRepository.save(inventory);

        return mapToResponse(inventory);
    }

    @Override
    public InventoryResponse getInventory(Long productId) {

        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found"));

        return mapToResponse(inventory);
    }

    @Override
    public InventoryResponse addStock(
            Long productId,
            Integer quantity) {

        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found"));

        inventory.setQuantity(
                inventory.getQuantity() + quantity);

        inventoryRepository.save(inventory);

        return mapToResponse(inventory);
    }

    @Override
    public InventoryResponse reduceStock(
            Long productId,
            Integer quantity) {

        Inventory inventory = inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found"));

        if (inventory.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock");
        }

        inventory.setQuantity(
                inventory.getQuantity() - quantity);

        inventoryRepository.save(inventory);

        return mapToResponse(inventory);
    }

    private InventoryResponse mapToResponse(
            Inventory inventory) {

        return InventoryResponse.builder()
                .id(inventory.getId())
                .productId(inventory.getProductId())
                .quantity(inventory.getQuantity())
                .build();
    }
}