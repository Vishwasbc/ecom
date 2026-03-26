package com.example.orders.service;

import com.example.orders.dto.CartItemRequest;
import com.example.orders.model.CartItem;

import java.util.List;

public interface CartService {
    boolean addToCart(Long userId, CartItemRequest request);

    boolean deleteItemFromCart(Long userId, Long productId);

    List<CartItem> getCart(Long userId);

    void clearCart(Long userId);
}
