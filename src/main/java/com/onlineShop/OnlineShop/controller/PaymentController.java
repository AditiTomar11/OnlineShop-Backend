package com.onlineShop.OnlineShop.controller;

import com.onlineShop.OnlineShop.entity.Payment;
import com.onlineShop.OnlineShop.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<Payment> pay(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentService.processDummyPayment(orderId));
    }
}