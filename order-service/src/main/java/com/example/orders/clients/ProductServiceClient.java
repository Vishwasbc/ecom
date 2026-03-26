package com.example.orders.clients;

import com.example.orders.dto.ProductResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface ProductServiceClient {
    @GetExchange("/api/products/findById/{id}")
    ProductResponse getProductDetails(@PathVariable String id);
}
