package com.onlineShop.OnlineShop.service;

import com.onlineShop.OnlineShop.entity.Order;
import com.onlineShop.OnlineShop.entity.OrderStatus;
import com.onlineShop.OnlineShop.entity.Payment;
import com.onlineShop.OnlineShop.repository.OrderRepository;
import com.onlineShop.OnlineShop.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public Payment processDummyPayment(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new RuntimeException("Order must be approved before payment");
        }

        // Dummy gateway — hamesha success (real integration baad mein Razorpay se replace hoga)
        Payment payment = Payment.builder()
                .order(order)
                .amount(order.getTotalAmount())
                .transactionId("TXN-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase())
                .status("SUCCESS")
                .paidAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);

        return payment;
    }
}