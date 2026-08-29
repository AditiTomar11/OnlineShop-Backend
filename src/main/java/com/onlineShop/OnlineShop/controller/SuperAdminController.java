package com.onlineShop.OnlineShop.controller;

import com.onlineShop.OnlineShop.dto.RegisterRequest;
import com.onlineShop.OnlineShop.entity.*;
import com.onlineShop.OnlineShop.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/superadmin")
public class SuperAdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SuperAdminController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create-admin")
    public ResponseEntity<String> createAdmin(@RequestBody RegisterRequest request) {
        User admin = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .role(Role.ADMIN)
                .enabled(true)
                .build();

        userRepository.save(admin);
        return ResponseEntity.ok("Admin created successfully");
    }
}
