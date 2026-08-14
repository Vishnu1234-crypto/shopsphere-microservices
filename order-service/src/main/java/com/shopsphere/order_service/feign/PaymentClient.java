package com.shopsphere.order_service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.shopsphere.order_service.dto.PaymentRequest;
import com.shopsphere.order_service.dto.PaymentResponse;

@FeignClient(name = "PAYMENT-SERVICE")
public interface PaymentClient {

    @PostMapping("/api/payments")
    PaymentResponse createPayment(
            @RequestBody PaymentRequest request
    );

    @GetMapping("/api/payments/order/{orderId}")
    PaymentResponse getPaymentByOrderId(
            @PathVariable Long orderId
    );

    /*
     * PaymentController uses:
     *
     * @PostMapping("/{id}/confirm")
     */
    @PostMapping("/api/payments/{id}/confirm")
    PaymentResponse confirmPayment(
            @PathVariable Long id
    );

    /*
     * PaymentController uses:
     *
     * @DeleteMapping("/{id}/cancel")
     */
    @DeleteMapping("/api/payments/{id}/cancel")
    void cancelPayment(
            @PathVariable Long id
    );

    /*
     * Keep this only if you have already implemented
     * POST /api/payments/{id}/refund in PaymentController.
     */
    @PostMapping("/api/payments/{id}/refund")
    PaymentResponse refundPayment(
            @PathVariable Long id
    );
}