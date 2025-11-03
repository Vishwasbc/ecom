package com.example.ecom.controller;

import com.example.ecom.dto.CartItemRequest;
import com.example.ecom.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") String userId,
            @RequestBody CartItemRequest request
            ){
        if(!cartService.addToCart(userId, request)){
            return ResponseEntity.badRequest().body("Product Out of Stock OR User not found OR Product not found");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
