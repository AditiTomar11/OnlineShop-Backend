package com.onlineShop.OnlineShop.controller;

import com.onlineShop.OnlineShop.dto.OrderResponse;
import com.onlineShop.OnlineShop.dto.RejectOrderRequest;
import com.onlineShop.OnlineShop.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;

    public AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<OrderResponse>> getPendingOrders() {
        return ResponseEntity.ok(orderService.getPendingOrders());
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping("/{orderId}/approve")
    public ResponseEntity<OrderResponse> approveOrder(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.approveOrder(orderId));
    }

    @PostMapping("/{orderId}/reject")
    public ResponseEntity<OrderResponse> rejectOrder(@PathVariable Long orderId, @RequestBody RejectOrderRequest request) {
        return ResponseEntity.ok(orderService.rejectOrder(orderId, request.getReason()));
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long orderId, @RequestParam String status) {
        return ResponseEntity.ok(orderService.updateStatus(orderId, com.onlineShop.OnlineShop.entity.OrderStatus.valueOf(status)));
    }
}