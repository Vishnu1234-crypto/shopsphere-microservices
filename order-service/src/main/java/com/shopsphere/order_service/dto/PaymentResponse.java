package com.shopsphere.order_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse {

    private Long id;

    private Long orderId;

    private Double amount;

    private String paymentMethod;

    private String upiId;

    private String transactionId;

    private String status;
}