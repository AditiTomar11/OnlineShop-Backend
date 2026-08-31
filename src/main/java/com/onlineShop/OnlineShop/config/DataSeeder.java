package com.onlineShop.OnlineShop.config;

import com.onlineShop.OnlineShop.entity.Role;
import com.onlineShop.OnlineShop.entity.User;
import com.onlineShop.OnlineShop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner seedSuperAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${superadmin.email:superadmin@onlineshop.com}") String email,
            @Value("${superadmin.password:ChangeMe@123}") String password) {
        return args -> {
            if (!userRepository.existsByRole(Role.SUPERADMIN)) {
                User superAdmin = User.builder()
                        .email(email)
                        .password(passwordEncoder.encode(password))
                        .fullName("Super Admin")
                        .role(Role.SUPERADMIN)
                        .enabled(true)
                        .build();
                userRepository.save(superAdmin);
                System.out.println("✅ SUPERADMIN seeded: " + email);
            }
        };
    }
}