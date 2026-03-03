package com.example.ecom.service;

import com.example.ecom.dto.CartItemRequest;
import com.example.ecom.model.CartItem;

import java.util.List;

public interface CartService {
    boolean addToCart(String userId, CartItemRequest request);

    boolean deleteItemFromCart(String userId, Long productId);

    List<CartItem> getCart(String userId);

    void clearCart(String userId);
}
