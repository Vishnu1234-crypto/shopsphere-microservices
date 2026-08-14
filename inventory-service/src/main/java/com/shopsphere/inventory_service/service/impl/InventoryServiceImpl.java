package com.shopsphere.inventory_service.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopsphere.inventory_service.dto.InventoryRequest;
import com.shopsphere.inventory_service.dto.InventoryResponse;
import com.shopsphere.inventory_service.entity.Inventory;
import com.shopsphere.inventory_service.exception.InsufficientStockException;
import com.shopsphere.inventory_service.exception.ResourceNotFoundException;
import com.shopsphere.inventory_service.repository.InventoryRepository;
import com.shopsphere.inventory_service.service.InventoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl
        implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public InventoryResponse createInventory(
            InventoryRequest request) {

        if (inventoryRepository.existsByProductId(
                request.getProductId())) {

            throw new IllegalArgumentException(
                    "Inventory already exists for product id: "
                            + request.getProductId()
            );
        }

        Inventory inventory =
                Inventory.builder()
                        .productId(request.getProductId())
                        .quantity(request.getQuantity())
                        .reservedQuantity(0)
                        .build();

        Inventory savedInventory =
                inventoryRepository.save(inventory);

        return mapToResponse(savedInventory);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getAllInventory() {

        return inventoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getInventoryByProductId(
            Long productId) {

        Inventory inventory =
                findByProductId(productId);

        return mapToResponse(inventory);
    }

    @Override
    @Transactional
    public InventoryResponse updateInventory(
            Long productId,
            InventoryRequest request) {

        Inventory inventory =
                findByProductId(productId);

        inventory.setQuantity(
                request.getQuantity()
        );

        if (inventory.getReservedQuantity()
                > inventory.getQuantity()) {

            throw new IllegalArgumentException(
                    "Quantity cannot be less than reserved quantity"
            );
        }

        Inventory updatedInventory =
                inventoryRepository.save(inventory);

        return mapToResponse(updatedInventory);
    }

    @Override
    @Transactional
    public InventoryResponse increaseStock(
            Long productId,
            Integer quantity) {

        validateQuantity(quantity);

        Inventory inventory =
                findByProductId(productId);

        inventory.setQuantity(
                inventory.getQuantity() + quantity
        );

        Inventory updatedInventory =
                inventoryRepository.save(inventory);

        return mapToResponse(updatedInventory);
    }

    @Override
    @Transactional
    public InventoryResponse reduceStock(
            Long productId,
            Integer quantity) {

        validateQuantity(quantity);

        Inventory inventory =
                findByProductId(productId);

        int availableQuantity =
                getAvailableQuantity(inventory);

        if (quantity > availableQuantity) {

            throw new InsufficientStockException(
                    "Insufficient stock for product id: "
                            + productId
                            + ". Available: "
                            + availableQuantity
                            + ", Requested: "
                            + quantity
            );
        }

        inventory.setQuantity(
                inventory.getQuantity() - quantity
        );

        Inventory updatedInventory =
                inventoryRepository.save(inventory);

        return mapToResponse(updatedInventory);
    }

    @Override
    @Transactional
    public InventoryResponse restoreStock(
            Long productId,
            Integer quantity) {

        validateQuantity(quantity);

        Inventory inventory =
                findByProductId(productId);

        inventory.setQuantity(
                inventory.getQuantity() + quantity
        );

        Inventory updatedInventory =
                inventoryRepository.save(inventory);

        return mapToResponse(updatedInventory);
    }

    @Override
    @Transactional
    public void deleteInventory(
            Long productId) {

        Inventory inventory =
                findByProductId(productId);

        inventoryRepository.delete(inventory);
    }

    private Inventory findByProductId(
            Long productId) {

        return inventoryRepository
                .findByProductId(productId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Inventory not found for product id: "
                                        + productId
                        )
                );
    }

    private int getAvailableQuantity(
            Inventory inventory) {

        return inventory.getQuantity()
                - inventory.getReservedQuantity();
    }

    private void validateQuantity(
            Integer quantity) {

        if (quantity == null || quantity <= 0) {

            throw new IllegalArgumentException(
                    "Quantity must be greater than 0"
            );
        }
    }

    private InventoryResponse mapToResponse(
            Inventory inventory) {

        return InventoryResponse.builder()
                .id(inventory.getId())
                .productId(inventory.getProductId())
                .quantity(inventory.getQuantity())
                .reservedQuantity(
                        inventory.getReservedQuantity()
                )
                .availableQuantity(
                        getAvailableQuantity(inventory)
                )
                .build();
    }
}