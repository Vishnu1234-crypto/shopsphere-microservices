package com.shopsphere.payment_service.service;

import java.util.List;

import com.shopsphere.payment_service.dto.PaymentRequest;
import com.shopsphere.payment_service.dto.PaymentResponse;

public interface PaymentService {

    PaymentResponse makePayment(PaymentRequest request);

    List<PaymentResponse> getAllPayments();

    PaymentResponse getPaymentById(Long id);

    PaymentResponse confirmPayment(Long id);

    PaymentResponse getPaymentByOrderId(Long orderId);

    void cancelPayment(Long id);

    PaymentResponse refundPayment(Long id);
}