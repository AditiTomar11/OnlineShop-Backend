package com.onlineShop.OnlineShop.controller;

import com.onlineShop.OnlineShop.dto.UpdateRoleRequest;
import com.onlineShop.OnlineShop.dto.UserResponse;
import com.onlineShop.OnlineShop.entity.Role;
import com.onlineShop.OnlineShop.entity.User;
import com.onlineShop.OnlineShop.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/superadmin/users")
public class SuperAdminUserController {

    private final UserRepository userRepository;

    public SuperAdminUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userRepository.findAll().stream()
                .map(u -> UserResponse.builder()
                        .id(u.getId())
                        .email(u.getEmail())
                        .fullName(u.getFullName())
                        .role(u.getRole().name())
                        .enabled(u.isEnabled())
                        .build())
                .toList();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<String> updateRole(@PathVariable Long id, @RequestBody UpdateRoleRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(Role.valueOf(request.getRole()));
        userRepository.save(user);
        return ResponseEntity.ok("Role updated successfully");
    }

    @PutMapping("/{id}/toggle-status")
    public ResponseEntity<String> toggleStatus(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEnabled(!user.isEnabled());
        userRepository.save(user);
        return ResponseEntity.ok(user.isEnabled() ? "User enabled" : "User disabled");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() == Role.SUPERADMIN) {
            return ResponseEntity.badRequest().body("Cannot delete a SUPERADMIN account");
        }

        userRepository.delete(user);
        return ResponseEntity.ok("User deleted successfully");
    }
}