package com.example.ecom.service;

import com.example.ecom.dto.CartItemRequest;
import com.example.ecom.model.CartItem;

import java.util.List;

public interface CartService {
    boolean addToCart(Long userId, CartItemRequest request);

    boolean deleteItemFromCart(Long userId, Long productId);

    List<CartItem> getCart(Long userId);

    void clearCart(Long userId);
}
