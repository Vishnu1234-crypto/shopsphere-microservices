package com.shopsphere.cart_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.shopsphere.cart_service.dto.OrderRequest;
import com.shopsphere.cart_service.dto.OrderResponse;

@FeignClient(name = "ORDER-SERVICE")
public interface OrderClient {

    @PostMapping("/api/orders")
    OrderResponse createOrder(
            @RequestBody OrderRequest request
    );

    @PostMapping("/api/orders/{id}/pay")
    OrderResponse payOrder(
            @PathVariable Long id
    );
}