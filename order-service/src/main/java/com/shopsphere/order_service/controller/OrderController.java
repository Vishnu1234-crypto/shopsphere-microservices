package com.shopsphere.order_service.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopsphere.order_service.dto.OrderRequest;
import com.shopsphere.order_service.dto.OrderResponse;
import com.shopsphere.order_service.service.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(
            @Valid @RequestBody OrderRequest request) {

        return ResponseEntity.ok(
                orderService.placeOrder(request)
        );
    }


    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {

        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                orderService.getOrderById(id)
        );
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> cancelOrder(
            @PathVariable Long id) {

        orderService.cancelOrder(id);

        return ResponseEntity.ok(
                "Order cancelled successfully"
        );
    }


    @PostMapping("/{id}/confirm-payment")
    public ResponseEntity<OrderResponse> confirmPayment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                orderService.confirmPayment(id)
        );
    }


    @PostMapping("/{id}/pay")
    public ResponseEntity<OrderResponse> payOrder(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                orderService.payOrder(id)
        );
    }
}