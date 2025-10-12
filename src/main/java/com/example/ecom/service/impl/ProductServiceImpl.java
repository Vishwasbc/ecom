package com.example.ecom.service.impl;

import com.example.ecom.dto.ProductRequest;
import com.example.ecom.dto.ProductResponse;
import com.example.ecom.model.Product;
import com.example.ecom.repository.ProductRepository;
import com.example.ecom.service.ProductService;
import com.example.ecom.utility.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Added for transaction management

import java.util.Optional;

/**
 * Service implementation for product-related business logic.
 * All public methods are transactional to ensure data integrity.
 */
@Service
@RequiredArgsConstructor
@Transactional // Ensures all public methods are transactional
public class ProductServiceImpl implements ProductService {

    // Injects the ProductRepository dependency
    private final ProductRepository productRepository;

    /**
     * Creates a new product and returns its response DTO.
     * Maps the incoming ProductRequest DTO to a Product entity, saves it, and returns a ProductResponse DTO.
     * @param productRequest DTO containing product details
     * @return ProductResponse DTO of the saved product
     */
    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product(); // Create a new Product entity
        Product savedProduct = productRepository.save(ProductMapper.mapProductRequestToProduct(product,productRequest)); // Map and save
        return ProductMapper.mapProductToProductResponse(savedProduct); // Map to response DTO
    }

    /**
     * Updates an existing product by ID.
     * Finds the product by ID, updates its fields using the ProductRequest DTO, saves it, and returns a ProductResponse DTO.
     * @param id Product ID
     * @param productRequest DTO containing updated product details
     * @return Optional<ProductResponse> DTO of the updated product if found
     */
    @Override
    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(existingProduct ->{
                    Product savedProduct = productRepository.save(ProductMapper.mapProductRequestToProduct(existingProduct,productRequest)); // Update and save
                    return ProductMapper.mapProductToProductResponse(savedProduct); // Map to response DTO
                });
    }
}
