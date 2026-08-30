package com.onlineShop.OnlineShop.controller;

import com.onlineShop.OnlineShop.dto.AddToCartRequest;
import com.onlineShop.OnlineShop.dto.CartItemResponse;
import com.onlineShop.OnlineShop.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity<String> addToCart(Authentication auth, @RequestBody AddToCartRequest request) {
        cartService.addToCart(auth.getName(), request);
        return ResponseEntity.ok("Added to cart");
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(Authentication auth) {
        return ResponseEntity.ok(cartService.getCart(auth.getName()));
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<String> updateQuantity(Authentication auth, @PathVariable Long cartItemId, @RequestParam Integer quantity) {
        cartService.updateQuantity(auth.getName(), cartItemId, quantity);
        return ResponseEntity.ok("Cart updated");
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<String> removeFromCart(Authentication auth, @PathVariable Long cartItemId) {
        cartService.removeFromCart(auth.getName(), cartItemId);
        return ResponseEntity.ok("Item removed");
    }
}