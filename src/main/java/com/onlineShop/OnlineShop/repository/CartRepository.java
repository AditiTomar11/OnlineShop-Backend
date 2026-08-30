package com.onlineShop.OnlineShop.repository;

import com.onlineShop.OnlineShop.entity.CartItem;
import com.onlineShop.OnlineShop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUser(User user);
    Optional<CartItem> findByUserAndProductId(User user, Long productId);
    void deleteByUser(User user);
}