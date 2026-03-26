package com.example.orders.service;

import com.example.orders.dto.OrderResponse;

import java.util.Optional;

public interface OrderService {
    Optional<OrderResponse> createOrder(Long userId);
}
