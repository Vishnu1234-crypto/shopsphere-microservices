package com.shopsphere.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;

    private Long orderId;

    private Double amount;

    private String paymentMethod;

    private String upiId;

    private String transactionId;

    private String status;
}