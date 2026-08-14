package com.shopsphere.inventory_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.shopsphere.inventory_service.entity.Inventory;

@Repository
public interface InventoryRepository
        extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByProductId(
            Long productId
    );

    boolean existsByProductId(
            Long productId
    );
}