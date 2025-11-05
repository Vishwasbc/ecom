package com.example.ecom.controller;

import com.example.ecom.dto.ProductRequest;
import com.example.ecom.dto.ProductResponse;
import com.example.ecom.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Slf4j
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.info("Fetching all active products");
        List<ProductResponse> products = productService.getAllProducts();
        log.debug("Fetched {} products", products.size());
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        log.info("Creating new product with name={}", productRequest.getName());
        ProductResponse response = productService.createProduct(productRequest);
        log.info("Product created successfully with id={}", response.getId());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        log.info("Updating product with id={}", id);
        return productService.updateProduct(id, productRequest)
                .map(updated -> {
                    log.info("Product updated successfully with id={}", id);
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> {
                    log.warn("Product not found with id={}, update failed", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product with id={}", id);
        boolean deleted = productService.deleteProduct(id);
        if (deleted) {
            log.info("Product soft deleted with id={}", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Product not found with id={}, delete failed", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(@RequestParam String keyword) {
        log.info("Searching products with keyword='{}'", keyword);
        List<ProductResponse> results = productService.searchProducts(keyword);
        log.debug("Found {} products matching keyword='{}'", results.size(), keyword);
        return ResponseEntity.ok(results);
    }
}