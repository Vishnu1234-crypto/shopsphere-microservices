package com.shopsphere.order_service.service;

import java.util.List;

import com.shopsphere.order_service.dto.OrderRequest;
import com.shopsphere.order_service.dto.OrderResponse;

public interface OrderService {

    OrderResponse placeOrder(OrderRequest request);

    List<OrderResponse> getAllOrders();

    OrderResponse getOrderById(Long id);

    OrderResponse confirmPayment(Long orderId);

    String cancelOrder(Long id);

    OrderResponse payOrder(Long orderId);
}