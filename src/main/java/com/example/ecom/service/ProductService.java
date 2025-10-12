package com.example.ecom.service;

import com.example.ecom.dto.ProductRequest;
import com.example.ecom.dto.ProductResponse;

import java.util.Optional;

public interface ProductService {

    ProductResponse createProduct(ProductRequest productRequest);

    Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest);
}
