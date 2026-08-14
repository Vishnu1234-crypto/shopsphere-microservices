package com.shopsphere.payment_service.service.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.shopsphere.payment_service.dto.PaymentRequest;
import com.shopsphere.payment_service.dto.PaymentResponse;
import com.shopsphere.payment_service.entity.Payment;
import com.shopsphere.payment_service.exception.ResourceNotFoundException;
import com.shopsphere.payment_service.repository.PaymentRepository;
import com.shopsphere.payment_service.service.PaymentService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    
    @Override
    public PaymentResponse makePayment(
            PaymentRequest request) {

        Payment existingPayment =
                paymentRepository
                        .findByOrderId(
                                request.getOrderId()
                        )
                        .orElse(null);

        if (existingPayment != null) {

            if ("SUCCESS".equals(
                    existingPayment.getStatus())) {

                throw new IllegalStateException(
                        "Payment already completed for order id: "
                                + request.getOrderId()
                );
            }

            if ("PENDING".equals(
                    existingPayment.getStatus())) {

                return mapToResponse(existingPayment);
            }
        }

        Payment payment = Payment.builder()
                .orderId(request.getOrderId())
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .upiId(request.getUpiId())
                .transactionId(generateTransactionId())
                .status("PENDING")
                .build();

        Payment savedPayment =
                paymentRepository.save(payment);

        return mapToResponse(savedPayment);
    }
    
    
    @Override
    public List<PaymentResponse> getAllPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponse getPaymentById(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with id: " + id
                        )
                );

        return mapToResponse(payment);
    }

    @Override
    public PaymentResponse confirmPayment(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with id: " + id
                        )
                );

        if ("SUCCESS".equals(payment.getStatus())) {
            return mapToResponse(payment);
        }

        if ("CANCELLED".equals(payment.getStatus())) {
            throw new IllegalStateException(
                    "Cancelled payment cannot be confirmed"
            );
        }

        payment.setStatus("SUCCESS");

        Payment updatedPayment = paymentRepository.save(payment);

        return mapToResponse(updatedPayment);
    }
    @Override
    public void cancelPayment(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found with id: " + id
                        )
                );

        if ("SUCCESS".equals(payment.getStatus())) {
            throw new IllegalStateException(
                    "Successful payment cannot be cancelled"
            );
        }

        if ("CANCELLED".equals(payment.getStatus())) {
            throw new IllegalStateException(
                    "Payment is already cancelled"
            );
        }

        payment.setStatus("CANCELLED");

        paymentRepository.save(payment);
    }
    private String generateTransactionId() {

        return "UPI-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    private PaymentResponse mapToResponse(Payment payment) {

        return PaymentResponse.builder()
                .id(payment.getId())
                .orderId(payment.getOrderId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .upiId(payment.getUpiId())
                .transactionId(payment.getTransactionId())
                .status(payment.getStatus())
                .build();
    }

    @Override
    public PaymentResponse getPaymentByOrderId(Long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Payment not found for order id: " + orderId
                        )
                );

        return mapToResponse(payment);
    }

    @Override
    public PaymentResponse refundPayment(Long id) {

        Payment payment =
                paymentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Payment not found with id: " + id
                                )
                        );

        if ("PENDING".equals(payment.getStatus())) {

            throw new IllegalStateException(
                    "Pending payment cannot be refunded"
            );
        }

        if ("CANCELLED".equals(payment.getStatus())) {

            throw new IllegalStateException(
                    "Cancelled payment cannot be refunded"
            );
        }

        if ("REFUNDED".equals(payment.getStatus())) {

            throw new IllegalStateException(
                    "Payment is already refunded"
            );
        }

        if ("SUCCESS".equals(payment.getStatus())) {

            payment.setStatus("REFUNDED");

            Payment refundedPayment =
                    paymentRepository.save(payment);

            return mapToResponse(refundedPayment);
        }

        throw new IllegalStateException(
                "Payment cannot be refunded with status: "
                        + payment.getStatus()
        );
    }
}