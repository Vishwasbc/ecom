package com.example.orders.utility;

import com.example.orders.dto.OrderItemDTO;
import com.example.orders.dto.OrderResponse;
import com.example.orders.model.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class OrderMapper {
    public static OrderResponse mappedToOrderResponse(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getOrderItems().stream()
                        .map(item -> new OrderItemDTO(
                                item.getId(),
                                item.getProductId(),
                                item.getQuantity(),
                                item.getPrice(),
                                item.getPrice().multiply(new BigDecimal(item.getQuantity()))
                        )).toList(),
                order.getCreatedAt()
        );
    }
}
