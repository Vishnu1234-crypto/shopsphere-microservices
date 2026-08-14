package com.shopsphere.inventory_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopsphere.inventory_service.dto.InventoryRequest;
import com.shopsphere.inventory_service.dto.InventoryResponse;
import com.shopsphere.inventory_service.service.InventoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping
    public ResponseEntity<InventoryResponse> createInventory(
            @Valid @RequestBody InventoryRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(inventoryService.createInventory(request));
    }

    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllInventory() {

        return ResponseEntity.ok(
                inventoryService.getAllInventory()
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<InventoryResponse> getInventory(
            @PathVariable Long productId) {

        return ResponseEntity.ok(
                inventoryService.getInventoryByProductId(productId)
        );
    }

    @PutMapping("/{productId}")
    public ResponseEntity<InventoryResponse> updateInventory(
            @PathVariable Long productId,
            @Valid @RequestBody InventoryRequest request) {

        return ResponseEntity.ok(
                inventoryService.updateInventory(
                        productId,
                        request
                )
        );
    }

    @PutMapping("/{productId}/increase")
    public ResponseEntity<InventoryResponse> increaseStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        return ResponseEntity.ok(
                inventoryService.increaseStock(
                        productId,
                        quantity
                )
        );
    }

    @PutMapping("/{productId}/reduce")
    public ResponseEntity<InventoryResponse> reduceStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        return ResponseEntity.ok(
                inventoryService.reduceStock(
                        productId,
                        quantity
                )
        );
    }

    @PutMapping("/{productId}/restore")
    public ResponseEntity<InventoryResponse> restoreStock(
            @PathVariable Long productId,
            @RequestParam Integer quantity) {

        return ResponseEntity.ok(
                inventoryService.restoreStock(
                        productId,
                        quantity
                )
        );
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteInventory(
            @PathVariable Long productId) {

        inventoryService.deleteInventory(productId);

        return ResponseEntity.ok(
                "Inventory deleted successfully"
        );
    }
}