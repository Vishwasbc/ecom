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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
     *
     * @param productRequest DTO containing product details
     * @return ProductResponse DTO of the saved product
     */
    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product = new Product(); // Create a new Product entity
        Product savedProduct = productRepository.save(ProductMapper.mapProductRequestToProduct(product, productRequest)); // Map and save
        return ProductMapper.mapProductToProductResponse(savedProduct); // Map to response DTO
    }

    /**
     * Updates an existing product by ID.
     * Finds the product by ID, updates its fields using the ProductRequest DTO, saves it, and returns a ProductResponse DTO.
     *
     * @param id             Product ID
     * @param productRequest DTO containing updated product details
     * @return Optional<ProductResponse> DTO of the updated product if found
     */
    @Override
    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    Product savedProduct = productRepository.save(ProductMapper.mapProductRequestToProduct(existingProduct, productRequest)); // Update and save
                    return ProductMapper.mapProductToProductResponse(savedProduct); // Map to response DTO
                });
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue()
                .stream()
                .map(ProductMapper::mapProductToProductResponse) // Map each Product to ProductResponse
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false); // Soft delete by setting active to false
                    productRepository.save(product); // Save the updated product
                    return true;
                }).orElse(false); // Return false if product not found
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword)
                .stream()
                .map(ProductMapper::mapProductToProductResponse) // Map each Product to ProductResponse
                .collect(Collectors.toList());
    }
}
