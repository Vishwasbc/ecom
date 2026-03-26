package com.example.orders.service.impl;

import com.example.orders.dto.OrderResponse;
import com.example.orders.model.*;
import com.example.orders.model.CartItem;
import com.example.orders.model.Order;
import com.example.orders.model.OrderItem;
import com.example.orders.model.OrderStatus;
import com.example.orders.repository.OrderRepository;
import com.example.orders.service.CartService;
import com.example.orders.service.OrderService;
import com.example.orders.utility.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final CartService cartService;
    private final OrderRepository orderRepository;

    @Override
    public Optional<OrderResponse> createOrder(Long userId) {
        log.info("Attempting to create order for userId={}", userId);
        List<CartItem> cartItems = cartService.getCart(userId);
        if (cartItems.isEmpty()) {
            log.warn("Cart is empty for userId={}", userId);
            return Optional.empty();
        }
        // Use userId directly (no User entity lookup inside order-service)
        BigDecimal totalPrice = cartItems.stream()
                .map(item->item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("Total price for userId={} is {}", userId, totalPrice);
        Order order = new Order();
        order.setUserId(userId);
        order.setTotalAmount(totalPrice);
        order.setStatus(OrderStatus.CONFIRMED);
        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                item.getProductId(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                ))
                .toList();
        order.setOrderItems(orderItems);
        log.debug("Order prepared with {} items for userId={}", orderItems.size(), userId);
        Order savedOrder = orderRepository.save(order);
        log.info("Order saved with id={} for userId={}", savedOrder.getId(), userId);
        cartService.clearCart(userId);
        log.info("Cart cleared for userId={}", userId);
        return Optional.of(OrderMapper.mappedToOrderResponse(savedOrder));
    }
}