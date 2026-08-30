package com.onlineShop.OnlineShop.repository;

import com.onlineShop.OnlineShop.entity.Order;
import com.onlineShop.OnlineShop.entity.OrderStatus;
import com.onlineShop.OnlineShop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerOrderByCreatedAtDesc(User customer);
    List<Order> findByStatusOrderByCreatedAtDesc(OrderStatus status);
    List<Order> findAllByOrderByCreatedAtDesc();
}