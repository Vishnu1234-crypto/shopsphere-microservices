package com.shopsphere.cart_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopsphere.cart_service.dto.ProductResponse;

@FeignClient(name = "PRODUCT-SERVICE")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductResponse getProductById(
            @PathVariable Long id
    );
}