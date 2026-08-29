package com.onlineShop.OnlineShop.controller;

import com.onlineShop.OnlineShop.entity.Role;
import com.onlineShop.OnlineShop.entity.User;
import com.onlineShop.OnlineShop.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final UserRepository userRepository;

    public DashboardController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getDashboard(Authentication authentication) {
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow();

        Map<String, Object> response = new HashMap<>();
        response.put("name", user.getFullName());
        response.put("role", user.getRole());

        switch (user.getRole()) {
            case SUPERADMIN -> {
                response.put("widgets", List.of("manageAdmins", "allOrders", "allUsers", "systemSettings", "revenue"));
            }
            case ADMIN -> {
                response.put("widgets", List.of("manageProducts", "viewOrders", "manageCategories"));
            }
            case CUSTOMER -> {
                response.put("widgets", List.of("myOrders", "cart", "wishlist", "profile"));
            }
        }

        return ResponseEntity.ok(response);
    }
}