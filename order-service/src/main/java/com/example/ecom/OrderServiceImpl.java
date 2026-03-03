package com.example.ecom.service.impl;

import com.example.ecom.dto.OrderResponse;
import com.example.ecom.model.*;
import com.example.ecom.repository.OrderRepository;
import com.example.ecom.repository.UserRepository;
import com.example.ecom.service.CartService;
import com.example.ecom.service.OrderService;
import com.example.ecom.utility.OrderMapper;
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
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    public Optional<OrderResponse> createOrder(String userId) {
        log.info("Attempting to create order for userId={}", userId);
        List<CartItem> cartItems = cartService.getCart(userId);
        if (cartItems.isEmpty()) {
            log.warn("Cart is empty for userId={}", userId);
            return Optional.empty();
        }
        Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
        if (userOpt.isEmpty()) {
            log.error("User not found with id={}", userId);
            return Optional.empty();
        }
        User user = userOpt.get();
        log.debug("User found: {}", user.getFirstName());
        BigDecimal totalPrice = cartItems.stream()
                .map(item->item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.info("Total price for userId={} is {}", userId, totalPrice);
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalPrice);
        order.setStatus(OrderStatus.CONFIRMED);
        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProduct(),
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