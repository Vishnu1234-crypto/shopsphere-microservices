package com.shopsphere.cart_service.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shopsphere.cart_service.dto.CartRequest;
import com.shopsphere.cart_service.dto.CartResponse;
import com.shopsphere.cart_service.dto.InventoryResponse;
import com.shopsphere.cart_service.dto.OrderRequest;
import com.shopsphere.cart_service.dto.OrderResponse;
import com.shopsphere.cart_service.dto.ProductResponse;
import com.shopsphere.cart_service.entity.Cart;
import com.shopsphere.cart_service.exception.ResourceNotFoundException;
import com.shopsphere.cart_service.feign.InventoryClient;
import com.shopsphere.cart_service.feign.OrderClient;
import com.shopsphere.cart_service.feign.ProductClient;
import com.shopsphere.cart_service.repository.CartRepository;
import com.shopsphere.cart_service.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final ProductClient productClient;

    private final InventoryClient inventoryClient;

    private final OrderClient orderClient;


    @Override
    public CartResponse addToCart(CartRequest request) {

        // Verify product exists before adding to cart
        ProductResponse product =
                productClient.getProductById(request.getProductId());

        if (product == null) {
            throw new ResourceNotFoundException(
                    "Product not found with id: "
                            + request.getProductId()
            );
        }

        // Verify inventory exists
        InventoryResponse inventory =
                inventoryClient.getInventory(request.getProductId());

        if (inventory == null) {
            throw new ResourceNotFoundException(
                    "Inventory not found for product id: "
                            + request.getProductId()
            );
        }

        // Verify requested quantity is available
        if (request.getQuantity()
                > inventory.getAvailableQuantity()) {

            throw new IllegalArgumentException(
                    "Insufficient stock for product id: "
                            + request.getProductId()
                            + ". Available: "
                            + inventory.getAvailableQuantity()
                            + ", Requested: "
                            + request.getQuantity()
            );
        }

        Cart cart = Cart.builder()
                .userId(request.getUserId())
                .productId(request.getProductId())
                .quantity(request.getQuantity())
                .build();

        Cart savedCart = cartRepository.save(cart);

        return mapToResponse(savedCart);
    }


    @Override
    public List<CartResponse> getCartByUser(Long userId) {

        return cartRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Override
    public void removeFromCart(Long cartId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Cart item not found with id: "
                                        + cartId
                        )
                );

        cartRepository.delete(cart);
    }


    @Override
    public OrderResponse checkout(Long userId, String upiId) {

        // ---------------------------------------------------------
        // 1. Get cart items
        // ---------------------------------------------------------

        List<Cart> cartItems =
                cartRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {

            throw new ResourceNotFoundException(
                    "Cart is empty for user id: " + userId
            );
        }


        // ---------------------------------------------------------
        // 2. Validate every product and inventory
        //    BEFORE creating any order
        // ---------------------------------------------------------

        for (Cart cart : cartItems) {

            ProductResponse product =
                    productClient.getProductById(
                            cart.getProductId()
                    );

            if (product == null) {

                throw new ResourceNotFoundException(
                        "Product not found with id: "
                                + cart.getProductId()
                );
            }


            InventoryResponse inventory =
                    inventoryClient.getInventory(
                            cart.getProductId()
                    );

            if (inventory == null) {

                throw new ResourceNotFoundException(
                        "Inventory not found for product id: "
                                + cart.getProductId()
                );
            }


            if (inventory.getAvailableQuantity()
                    < cart.getQuantity()) {

                throw new IllegalArgumentException(
                        "Insufficient stock for product id: "
                                + cart.getProductId()
                                + ". Available: "
                                + inventory.getAvailableQuantity()
                                + ", Requested: "
                                + cart.getQuantity()
                );
            }
        }


        // ---------------------------------------------------------
        // 3. Create and pay orders
        //
        // Order Service will:
        //
        //    1. Create order
        //    2. Create payment
        //    3. Confirm payment
        //    4. Reduce inventory
        //    5. Mark order as PAID
        // ---------------------------------------------------------

        OrderResponse firstOrder = null;

        for (Cart cart : cartItems) {

            ProductResponse product =
                    productClient.getProductById(
                            cart.getProductId()
                    );


            // Calculate item total
            double itemTotal =
                    product.getPrice()
                            * cart.getQuantity();


            OrderRequest orderRequest =
                    new OrderRequest(
                            userId,
                            cart.getProductId(),
                            cart.getQuantity(),
                            itemTotal,
                            upiId
                    );


            // Create order
            OrderResponse order =
                    orderClient.createOrder(orderRequest);


            if (firstOrder == null) {
                firstOrder = order;
            }


            // Complete payment
            // Order Service handles inventory reduction
            // only after successful payment.
            orderClient.payOrder(order.getId());
        }


        // ---------------------------------------------------------
        // 4. Clear cart only after all orders are successful
        // ---------------------------------------------------------

        cartRepository.deleteAll(cartItems);


        return firstOrder;
    }


    private CartResponse mapToResponse(Cart cart) {

        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .productId(cart.getProductId())
                .quantity(cart.getQuantity())
                .build();
    }
}