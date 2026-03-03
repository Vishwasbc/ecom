package com.example.ecom.controller;

import com.example.ecom.dto.OrderResponse;
import com.example.ecom.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("X-User-ID") Long userId) {
        log.info("Received request to create order for userId={}", userId);

        Optional<OrderResponse> order = orderService.createOrder(userId);

        if (order.isPresent()) {
            log.info("Order created successfully for userId={} with orderId={}",
                    userId, order.get().getId());
            return new ResponseEntity<>(order.get(), HttpStatus.OK);
        } else {
            log.warn("Failed to create order for userId={}.", userId);
            return ResponseEntity.badRequest().build();
        }
    }
}