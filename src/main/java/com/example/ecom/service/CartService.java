package com.example.ecom.service;

import com.example.ecom.dto.CartItemRequest;

public interface CartService {
    boolean addToCart(String userId, CartItemRequest request);
}
