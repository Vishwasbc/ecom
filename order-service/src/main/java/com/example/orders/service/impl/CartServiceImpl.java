package com.example.orders.service.impl;

import com.example.orders.clients.ProductServiceClient;
import com.example.orders.clients.UserServiceClient;
import com.example.orders.dto.CartItemRequest;
import com.example.orders.dto.ProductResponse;
import com.example.orders.dto.UserResponse;
import com.example.orders.model.CartItem;
import com.example.orders.repository.CartItemRepository;
import com.example.orders.service.CartService;
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

    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;

    @Override
    public boolean addToCart(Long userId, CartItemRequest request) {
        log.info("Adding productId={} (qty={}) to cart for userId={}",
                request.getProductId(), request.getQuantity(), userId);
        ProductResponse product = productServiceClient.getProductDetails(request.getProductId().toString());
        if(product == null)
            return false;
        if(product.getStockQuantity()<request.getQuantity())
            return false;

        UserResponse user =  userServiceClient.getUserDetails(userId);
        if(user == null)
            return false;
        CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());

        if (existingCartItem != null) {
            int oldQty = existingCartItem.getQuantity() == null ? 0 : existingCartItem.getQuantity();
            int newQuantity = oldQty + request.getQuantity();
            existingCartItem.setQuantity(newQuantity);
            if (existingCartItem.getPrice() != null && oldQty > 0) {
                BigDecimal unitPrice = existingCartItem.getPrice().divide(BigDecimal.valueOf(oldQty));
                existingCartItem.setPrice(unitPrice.multiply(BigDecimal.valueOf(newQuantity)));
            } else {
                existingCartItem.setPrice(BigDecimal.ZERO);
            }
            cartItemRepository.save(existingCartItem);
            log.info("Updated cart item for userId={} productId={} newQuantity={}",
                    userId, request.getProductId(), newQuantity);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(BigDecimal.ZERO);
            cartItemRepository.save(cartItem);
            log.info("Created new cart item for userId={} productId={} quantity={}",
                    userId, request.getProductId(), request.getQuantity());
        }
        return true;
    }

    @Override
    public boolean deleteItemFromCart(Long userId, Long productId) {
        log.info("Deleting productId={} from cart for userId={}", productId, userId);

        CartItem existing = cartItemRepository.findByUserIdAndProductId(userId, productId);
        if (existing != null) {
            cartItemRepository.deleteByUserIdAndProductId(userId, productId);
            log.info("Deleted productId={} from cart for userId={}", productId, userId);
            return true;
        }

        log.warn("Delete failed: productId={} or userId={} not found", productId, userId);
        return false;
    }

    @Override
    public List<CartItem> getCart(Long userId) {
        log.info("Fetching cart for userId={}", userId);
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        log.debug("Found {} items in cart for userId={}", items.size(), userId);
        return items;
    }

    @Override
    public void clearCart(Long userId) {
        log.info("Clearing cart for userId={}", userId);
        cartItemRepository.deleteByUserId(userId);
        log.info("Cart cleared for userId={}", userId);
    }
}