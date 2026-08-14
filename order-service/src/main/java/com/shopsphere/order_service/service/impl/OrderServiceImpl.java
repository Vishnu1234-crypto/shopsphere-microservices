package com.shopsphere.order_service.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopsphere.order_service.dto.InventoryResponse;
import com.shopsphere.order_service.dto.NotificationRequest;
import com.shopsphere.order_service.dto.OrderRequest;
import com.shopsphere.order_service.dto.OrderResponse;
import com.shopsphere.order_service.dto.PaymentRequest;
import com.shopsphere.order_service.dto.PaymentResponse;
import com.shopsphere.order_service.entity.Order;
import com.shopsphere.order_service.exception.ResourceNotFoundException;
import com.shopsphere.order_service.feign.InventoryClient;
import com.shopsphere.order_service.feign.NotificationClient;
import com.shopsphere.order_service.feign.PaymentClient;
import com.shopsphere.order_service.repository.OrderRepository;
import com.shopsphere.order_service.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final InventoryClient inventoryClient;

    private final PaymentClient paymentClient;

    private final NotificationClient notificationClient;


    // ============================================================
    // PLACE ORDER
    // ============================================================

    @Override
    public OrderResponse placeOrder(OrderRequest request) {

        // --------------------------------------------------------
        // 1. Check inventory
        // --------------------------------------------------------

        InventoryResponse inventory =
                inventoryClient.getInventory(
                        request.getProductId()
                );

        if (inventory == null) {

            throw new ResourceNotFoundException(
                    "Inventory not found for product id: "
                            + request.getProductId()
            );
        }

        if (inventory.getAvailableQuantity()
                < request.getQuantity()) {

            throw new IllegalArgumentException(
                    "Insufficient stock for product id: "
                            + request.getProductId()
                            + ". Available: "
                            + inventory.getAvailableQuantity()
                            + ", Requested: "
                            + request.getQuantity()
            );
        }


        // --------------------------------------------------------
        // 2. Reduce inventory
        // --------------------------------------------------------

        inventoryClient.reduceStock(
                request.getProductId(),
                request.getQuantity()
        );


        // --------------------------------------------------------
        // 3. Create order
        // --------------------------------------------------------

        Order order = Order.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .totalPrice(request.getTotalPrice())
                .status("PLACED")
                .build();

        Order savedOrder =
                orderRepository.save(order);


        // --------------------------------------------------------
        // 4. Create payment
        // --------------------------------------------------------

        PaymentRequest paymentRequest =
                new PaymentRequest(
                        savedOrder.getId(),
                        savedOrder.getTotalPrice(),
                        "UPI",
                        request.getUpiId()
                );

        paymentClient.createPayment(
                paymentRequest
        );


        // --------------------------------------------------------
        // 5. Send notification
        // --------------------------------------------------------

        NotificationRequest notificationRequest =
                new NotificationRequest(
                        "Order placed successfully. Order ID: "
                                + savedOrder.getId()
                );

        notificationClient.sendNotification(
                notificationRequest
        );


        // --------------------------------------------------------
        // 6. Return order
        // --------------------------------------------------------

        return mapToResponse(savedOrder);
    }


    // ============================================================
    // GET ALL ORDERS
    // ============================================================

    @Override
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    // ============================================================
    // GET ORDER BY ID
    // ============================================================

    @Override
    public OrderResponse getOrderById(Long id) {

        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found with id: "
                                                + id
                                )
                        );

        return mapToResponse(order);
    }


    // ============================================================
    // CANCEL ORDER
    // ============================================================

    @Override
    @Transactional
    public String cancelOrder(Long id) {

        // --------------------------------------------------------
        // 1. Find order
        // --------------------------------------------------------

        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found with id: "
                                                + id
                                )
                        );


        // --------------------------------------------------------
        // 2. Check order status
        // --------------------------------------------------------

        if ("CANCELLED".equals(order.getStatus())) {

            throw new IllegalStateException(
                    "Order is already cancelled"
            );
        }


        // --------------------------------------------------------
        // 3. Get payment
        // --------------------------------------------------------

        PaymentResponse payment =
                paymentClient.getPaymentByOrderId(id);


        // --------------------------------------------------------
        // 4. Handle payment
        // --------------------------------------------------------

        if (payment != null) {

            // ----------------------------------------------------
            // PAID order
            // ----------------------------------------------------

            if ("SUCCESS".equals(payment.getStatus())) {

                // Successful payment cannot be cancelled.
                // It must be refunded.

                paymentClient.refundPayment(
                        payment.getId()
                );
            }


            // ----------------------------------------------------
            // PENDING payment
            // ----------------------------------------------------

            else if ("PENDING".equals(payment.getStatus())) {

                paymentClient.cancelPayment(
                        payment.getId()
                );
            }


            // ----------------------------------------------------
            // CANCELLED payment
            // ----------------------------------------------------

            else if ("CANCELLED".equals(
                    payment.getStatus())) {

                // Payment is already cancelled.
                // Nothing to do.
            }


            // ----------------------------------------------------
            // REFUNDED payment
            // ----------------------------------------------------

            else if ("REFUNDED".equals(
                    payment.getStatus())) {

                throw new IllegalStateException(
                        "Payment is already refunded"
                );
            }
        }


        // --------------------------------------------------------
        // 5. Restore inventory
        // --------------------------------------------------------

        inventoryClient.restoreStock(
                order.getProductId(),
                order.getQuantity()
        );


        // --------------------------------------------------------
        // 6. Update order status
        // --------------------------------------------------------

        order.setStatus("CANCELLED");

        Order updatedOrder =
                orderRepository.save(order);


        // --------------------------------------------------------
        // 7. Send notification
        // --------------------------------------------------------

        NotificationRequest notificationRequest =
                new NotificationRequest(
                        "Order cancelled successfully. Order ID: "
                                + updatedOrder.getId()
                );

        notificationClient.sendNotification(
                notificationRequest
        );


        return "Order cancelled successfully";
    }


    // ============================================================
    // CONFIRM PAYMENT
    // ============================================================

    @Override
    public OrderResponse confirmPayment(Long orderId) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found with id: "
                                                + orderId
                                )
                        );


        if ("CANCELLED".equals(order.getStatus())) {

            throw new IllegalStateException(
                    "Cancelled order cannot be paid"
            );
        }


        if ("PAID".equals(order.getStatus())) {

            return mapToResponse(order);
        }


        PaymentResponse payment =
                paymentClient.getPaymentByOrderId(
                        orderId
                );


        if ("SUCCESS".equals(payment.getStatus())) {

            order.setStatus("PAID");

            Order updatedOrder =
                    orderRepository.save(order);

            return mapToResponse(updatedOrder);
        }


        if ("CANCELLED".equals(payment.getStatus())) {

            throw new IllegalStateException(
                    "Cancelled payment cannot be confirmed"
            );
        }


        PaymentResponse confirmedPayment =
                paymentClient.confirmPayment(
                        payment.getId()
                );


        if (!"SUCCESS".equals(
                confirmedPayment.getStatus())) {

            throw new IllegalStateException(
                    "Payment confirmation failed"
            );
        }


        order.setStatus("PAID");

        Order updatedOrder =
                orderRepository.save(order);

        return mapToResponse(updatedOrder);
    }


    // ============================================================
    // PAY ORDER
    // ============================================================

    @Override
    public OrderResponse payOrder(Long orderId) {

        Order order =
                orderRepository.findById(orderId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Order not found with id: "
                                                + orderId
                                )
                        );


        if ("CANCELLED".equals(order.getStatus())) {

            throw new IllegalStateException(
                    "Cancelled order cannot be paid"
            );
        }


        if ("PAID".equals(order.getStatus())) {

            return mapToResponse(order);
        }


        PaymentResponse payment =
                paymentClient.getPaymentByOrderId(
                        orderId
                );


        if ("CANCELLED".equals(
                payment.getStatus())) {

            throw new IllegalStateException(
                    "Cancelled payment cannot be confirmed"
            );
        }


        PaymentResponse confirmedPayment;


        if ("SUCCESS".equals(payment.getStatus())) {

            confirmedPayment = payment;

        } else {

            confirmedPayment =
                    paymentClient.confirmPayment(
                            payment.getId()
                    );
        }


        if (!"SUCCESS".equals(
                confirmedPayment.getStatus())) {

            throw new IllegalStateException(
                    "Payment failed for order: "
                            + orderId
            );
        }


        order.setStatus("PAID");

        Order updatedOrder =
                orderRepository.save(order);

        return mapToResponse(updatedOrder);
    }


    // ============================================================
    // ENTITY → RESPONSE
    // ============================================================

    private OrderResponse mapToResponse(
            Order order) {

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