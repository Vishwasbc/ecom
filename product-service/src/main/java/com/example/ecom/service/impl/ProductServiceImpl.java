package com.example.ecom.service.impl;

import com.example.ecom.dto.ProductRequest;
import com.example.ecom.dto.ProductResponse;
import com.example.ecom.model.Product;
import com.example.ecom.repository.ProductRepository;
import com.example.ecom.service.ProductService;
import com.example.ecom.utility.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse createProduct(ProductRequest productRequest) {
        log.info("Creating new product with name={}", productRequest.getName());
        Product product = new Product();
        Product savedProduct = productRepository.save(
                ProductMapper.mapProductRequestToProduct(product, productRequest)
        );
        log.debug("Product created successfully with id={}", savedProduct.getId());
        return ProductMapper.mapProductToProductResponse(savedProduct);
    }

    @Override
    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        log.info("Updating product with id={}", id);
        return productRepository.findById(id)
                .map(existingProduct -> {
                    Product savedProduct = productRepository.save(
                            ProductMapper.mapProductRequestToProduct(existingProduct, productRequest)
                    );
                    log.debug("Product updated successfully with id={}", savedProduct.getId());
                    return ProductMapper.mapProductToProductResponse(savedProduct);
                });
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        log.info("Fetching all active products");
        List<ProductResponse> products = productRepository.findByActiveTrue()
                .stream()
                .map(ProductMapper::mapProductToProductResponse)
                .collect(Collectors.toList());
        log.debug("Fetched {} active products", products.size());
        return products;
    }

    @Override
    public boolean deleteProduct(Long id) {
        log.info("Soft deleting product with id={}", id);
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    log.info("Product soft deleted with id={}", id);
                    return true;
                })
                .orElseGet(() -> {
                    log.warn("Product not found with id={}, delete failed", id);
                    return false;
                });
    }

    @Override
    public List<ProductResponse> searchProducts(String keyword) {
        log.info("Searching products with keyword='{}'", keyword);
        List<ProductResponse> results = productRepository.searchProducts(keyword)
                .stream()
                .map(ProductMapper::mapProductToProductResponse)
                .collect(Collectors.toList());
        log.debug("Found {} products matching keyword='{}'", results.size(), keyword);
        return results;
    }
}
