package com.onlineShop.OnlineShop.service;

import com.onlineShop.OnlineShop.dto.AddToCartRequest;
import com.onlineShop.OnlineShop.dto.CartItemResponse;
import com.onlineShop.OnlineShop.entity.CartItem;
import com.onlineShop.OnlineShop.entity.Product;
import com.onlineShop.OnlineShop.entity.User;
import com.onlineShop.OnlineShop.repository.CartRepository;
import com.onlineShop.OnlineShop.repository.ProductRepository;
import com.onlineShop.OnlineShop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public void addToCart(String email, AddToCartRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow();
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getStock() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock available");
        }

        var existing = cartRepository.findByUserAndProductId(user, product.getId());

        if (existing.isPresent()) {
            CartItem item = existing.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartRepository.save(item);
        } else {
            CartItem item = CartItem.builder()
                    .user(user)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();
            cartRepository.save(item);
        }
    }

    public List<CartItemResponse> getCart(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return cartRepository.findByUser(user).stream()
                .map(item -> CartItemResponse.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .imageUrl(item.getProduct().getImageUrl())
                        .price(item.getProduct().getPrice())
                        .quantity(item.getQuantity())
                        .subtotal(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                        .build())
                .toList();
    }

    public void updateQuantity(String email, Long cartItemId, Integer quantity) {
        User user = userRepository.findByEmail(email).orElseThrow();
        CartItem item = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        if (quantity <= 0) {
            cartRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartRepository.save(item);
        }
    }

    public void removeFromCart(String email, Long cartItemId) {
        User user = userRepository.findByEmail(email).orElseThrow();
        CartItem item = cartRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        if (!item.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        cartRepository.delete(item);
    }
}