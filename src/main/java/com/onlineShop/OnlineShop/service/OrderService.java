package com.onlineShop.OnlineShop.service;

import com.onlineShop.OnlineShop.dto.OrderItemResponse;
import com.onlineShop.OnlineShop.dto.OrderResponse;
import com.onlineShop.OnlineShop.entity.*;
import com.onlineShop.OnlineShop.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, CartRepository cartRepository,
                        ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponse placeOrder(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        List<CartItem> cartItems = cartRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        BigDecimal total = BigDecimal.ZERO;
        Order order = Order.builder()
                .customer(user)
                .status(OrderStatus.PENDING_APPROVAL)
                .build();

        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            return OrderItem.builder()
                    .order(order)
                    .product(cartItem.getProduct())
                    .quantity(cartItem.getQuantity())
                    .priceAtPurchase(cartItem.getProduct().getPrice())
                    .build();
        }).toList();

        for (OrderItem item : orderItems) {
            total = total.add(item.getPriceAtPurchase().multiply(BigDecimal.valueOf(item.getQuantity())));
        }

        order.setItems(orderItems);
        order.setTotalAmount(total);

        Order saved = orderRepository.save(order);

        // Cart clear karo order place hone ke baad
        cartRepository.deleteByUser(user);

        return toResponse(saved);
    }

    // ADMIN: pending orders dekho
    public List<OrderResponse> getPendingOrders() {
        return orderRepository.findByStatusOrderByCreatedAtDesc(OrderStatus.PENDING_APPROVAL)
                .stream().map(this::toResponse).toList();
    }

    // ADMIN: sab orders dekho
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toResponse).toList();
    }

    // CUSTOMER: apne orders dekho
    public List<OrderResponse> getMyOrders(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return orderRepository.findByCustomerOrderByCreatedAtDesc(user)
                .stream().map(this::toResponse).toList();
    }

    @Transactional
    public OrderResponse approveOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING_APPROVAL) {
            throw new RuntimeException("Order is not pending approval");
        }

        // Stock check — asli availability verify karo
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for: " + product.getName());
            }
        }

        // Stock deduct karo
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(OrderStatus.APPROVED);
        return toResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse rejectOrder(Long orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(OrderStatus.REJECTED);
        order.setRejectionReason(reason);
        return toResponse(orderRepository.save(order));
    }

    @Transactional
    public OrderResponse updateStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(newStatus);
        return toResponse(orderRepository.save(order));
    }

    private OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(item -> OrderItemResponse.builder()
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .priceAtPurchase(item.getPriceAtPurchase())
                        .build())
                .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .customerName(order.getCustomer().getFullName())
                .customerEmail(order.getCustomer().getEmail())
                .items(items)
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus().name())
                .rejectionReason(order.getRejectionReason())
                .createdAt(order.getCreatedAt())
                .build();
    }
}