package com.onlineShop.OnlineShop.repository;


import com.onlineShop.OnlineShop.entity.Role;
import com.onlineShop.OnlineShop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    default boolean existsByRole(com.onlineShop.OnlineShop.entity.Role role) {
        return false;
    }

}