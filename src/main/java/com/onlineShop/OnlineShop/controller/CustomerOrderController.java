package com.onlineShop.OnlineShop.controller;

import com.onlineShop.OnlineShop.dto.OrderResponse;
import com.onlineShop.OnlineShop.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/orders")
public class CustomerOrderController {

    private final OrderService orderService;

    public CustomerOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(Authentication auth) {
        return ResponseEntity.ok(orderService.placeOrder(auth.getName()));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getMyOrders(Authentication auth) {
        return ResponseEntity.ok(orderService.getMyOrders(auth.getName()));
    }
}