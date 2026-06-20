package com.shopsphere.order_service.service.impl;

import com.shopsphere.order_service.dto.*;
import com.shopsphere.order_service.entity.Order;
import com.shopsphere.order_service.feign.InventoryClient;
import com.shopsphere.order_service.feign.NotificationClient;
import com.shopsphere.order_service.feign.PaymentClient;
import com.shopsphere.order_service.repository.OrderRepository;
import com.shopsphere.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final PaymentClient paymentClient;
    private final NotificationClient notificationClient;

    public OrderServiceImpl(
            OrderRepository orderRepository,
            InventoryClient inventoryClient,
            PaymentClient paymentClient,
            NotificationClient notificationClient) {

        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
        this.paymentClient = paymentClient;
        this.notificationClient = notificationClient;
    }
    @Override
    public OrderResponse placeOrder(OrderRequest request) {

        InventoryResponse inventory =
                inventoryClient.getInventory(request.getProductId());

        if (inventory.getQuantity() <= 0) {
            throw new RuntimeException("Product Out Of Stock");
        }

        Order order = Order.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .totalPrice(request.getTotalPrice())
                .status("PLACED")
                .build();

        Order savedOrder = orderRepository.save(order);

        PaymentRequest paymentRequest =
                new PaymentRequest(
                        savedOrder.getId(),
                        savedOrder.getTotalPrice(),
                        "UPI"
                );

        paymentClient.createPayment(paymentRequest);

        NotificationRequest notificationRequest =
                new NotificationRequest(
                        "Order placed successfully"
                );

        notificationClient.sendNotification(notificationRequest);

        return mapToResponse(savedOrder);
    } 
    
    
    
    
    
    
    
    
    
    

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public OrderResponse getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        return mapToResponse(order);
    }

    @Override
    public String cancelOrder(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order Not Found"));

        order.setStatus("CANCELLED");

        orderRepository.save(order);

        return "Order Cancelled Successfully";
    }

    private OrderResponse mapToResponse(Order order) {

        return OrderResponse.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .productId(order.getProductId())
                .quantity(order.getQuantity())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .build();
    }
}