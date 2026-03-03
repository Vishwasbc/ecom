package com.example.ecom.controller;

import com.example.ecom.dto.CartItemRequest;
import com.example.ecom.model.CartItem;
import com.example.ecom.service.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest request) {
        log.info("Received request to add productId={} (qty={}) to cart for userId={}",
                request.getProductId(), request.getQuantity(), userId);

        if (!cartService.addToCart(userId, request)) {
            log.warn("Failed to add productId={} to cart for userId={}. Reason: Out of stock or User/Product not found",
                    request.getProductId(), userId);
            return ResponseEntity.badRequest().body("Product Out of Stock or User not found or Product not found");
        }

        log.info("Successfully added productId={} to cart for userId={}", request.getProductId(), userId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestHeader("X-User-ID") String userId,
            @PathVariable Long productId) {
        log.info("Received request to remove productId={} from cart for userId={}", productId, userId);

        boolean deleted = cartService.deleteItemFromCart(userId, productId);
        if (deleted) {
            log.info("Successfully removed productId={} from cart for userId={}", productId, userId);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Failed to remove productId={} from cart for userId={}. Item not found", productId, userId);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(
            @RequestHeader("X-User-ID") String userId) {
        log.info("Fetching cart for userId={}", userId);
        List<CartItem> cartItems = cartService.getCart(userId);
        log.debug("Cart for userId={} contains {} items", userId, cartItems.size());
        return ResponseEntity.ok(cartItems);
    }
}