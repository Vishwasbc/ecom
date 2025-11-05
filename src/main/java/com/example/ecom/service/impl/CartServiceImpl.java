package com.example.ecom.service.impl;

import com.example.ecom.dto.CartItemRequest;
import com.example.ecom.model.CartItem;
import com.example.ecom.model.Product;
import com.example.ecom.model.User;
import com.example.ecom.repository.CartItemRepository;
import com.example.ecom.repository.ProductRepository;
import com.example.ecom.repository.UserRepository;
import com.example.ecom.service.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartServiceImpl implements CartService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Override
    public boolean addToCart(String userId, CartItemRequest request) {
        log.info("Adding productId={} (qty={}) to cart for userId={}",
                request.getProductId(), request.getQuantity(), userId);

        Optional<Product> productOpt = productRepository.findById(request.getProductId());
        if (productOpt.isEmpty()) {
            log.warn("Product not found with id={} for userId={}", request.getProductId(), userId);
            return false;
        }

        Product product = productOpt.get();
        if (product.getStockQuantity() < request.getQuantity()) {
            log.warn("Insufficient stock for productId={} (requested={}, available={})",
                    product.getId(), request.getQuantity(), product.getStockQuantity());
            return false;
        }

        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if (userOpt.isEmpty()) {
            log.error("User not found with id={}", userId);
            return false;
        }

        User user = userOpt.get();
        CartItem existingCartItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingCartItem != null) {
            int newQuantity = existingCartItem.getQuantity() + request.getQuantity();
            existingCartItem.setQuantity(newQuantity);
            existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(newQuantity)));
            cartItemRepository.save(existingCartItem);
            log.info("Updated cart item for userId={} productId={} newQuantity={}",
                    userId, product.getId(), newQuantity);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
            cartItemRepository.save(cartItem);
            log.info("Created new cart item for userId={} productId={} quantity={}",
                    userId, product.getId(), request.getQuantity());
        }
        return true;
    }

    @Override
    public boolean deleteItemFromCart(String userId, Long productId) {
        log.info("Deleting productId={} from cart for userId={}", productId, userId);

        Optional<Product> productOpt = productRepository.findById(productId);
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));

        if (productOpt.isPresent() && userOpt.isPresent()) {
            cartItemRepository.deleteByUserAndProduct(userOpt.get(), productOpt.get());
            log.info("Deleted productId={} from cart for userId={}", productId, userId);
            return true;
        }

        log.warn("Delete failed: productId={} or userId={} not found", productId, userId);
        return false;
    }

    @Override
    public List<CartItem> getCart(String userId) {
        log.info("Fetching cart for userId={}", userId);
        return userRepository.findById(Long.valueOf(userId))
                .map(user -> {
                    List<CartItem> items = cartItemRepository.findByUser(user);
                    log.debug("Found {} items in cart for userId={}", items.size(), userId);
                    return items;
                })
                .orElseGet(() -> {
                    log.warn("User not found with id={}, returning empty cart", userId);
                    return List.of();
                });
    }

    @Override
    public void clearCart(String userId) {
        log.info("Clearing cart for userId={}", userId);
        userRepository.findById(Long.valueOf(userId))
                .ifPresentOrElse(
                        user -> {
                            cartItemRepository.deleteByUser(user);
                            log.info("Cart cleared for userId={}", userId);
                        },
                        () -> log.warn("User not found with id={}, cannot clear cart", userId)
                );
    }
}