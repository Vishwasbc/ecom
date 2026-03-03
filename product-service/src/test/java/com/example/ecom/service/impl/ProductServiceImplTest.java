package com.example.ecom.service.impl;

import com.example.ecom.dto.ProductRequest;
import com.example.ecom.dto.ProductResponse;
import com.example.ecom.model.Product;
import com.example.ecom.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setName("Test Product");
        sampleProduct.setDescription("Desc");
        sampleProduct.setPrice(new BigDecimal("9.99"));
        sampleProduct.setStockQuantity(10);
        sampleProduct.setCategory("cat");
        sampleProduct.setImageUrl("/img.png");
        sampleProduct.setActive(true);
    }

    @Test
    void createProduct_shouldReturnResponse() {
        ProductRequest req = new ProductRequest();
        req.setName("Test Product");
        req.setDescription("Desc");
        req.setPrice(new BigDecimal("9.99"));
        req.setStockQuantity(10);
        req.setCategory("cat");
        req.setImageUrl("/img.png");

        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        ProductResponse resp = productService.createProduct(req);

        assertNotNull(resp);
        assertEquals(sampleProduct.getId(), resp.getId());
        assertEquals(sampleProduct.getName(), resp.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getAllProducts_shouldReturnActiveProducts() {
        when(productRepository.findByActiveTrue()).thenReturn(List.of(sampleProduct));

        List<ProductResponse> list = productService.getAllProducts();

        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("Test Product", list.get(0).getName());
    }

    @Test
    void updateProduct_whenExists_shouldReturnUpdated() {
        ProductRequest req = new ProductRequest();
        req.setName("Updated");

        Product updated = new Product();
        updated.setId(1L);
        updated.setName("Updated");

        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updated);

        Optional<ProductResponse> opt = productService.updateProduct(1L, req);

        assertTrue(opt.isPresent());
        assertEquals("Updated", opt.get().getName());
    }

    @Test
    void deleteProduct_whenExists_shouldReturnTrue() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(sampleProduct));
        when(productRepository.save(any(Product.class))).thenReturn(sampleProduct);

        boolean result = productService.deleteProduct(1L);

        assertTrue(result);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void searchProducts_shouldReturnMatches() {
        when(productRepository.searchProducts("test")).thenReturn(List.of(sampleProduct));

        List<ProductResponse> results = productService.searchProducts("test");

        assertEquals(1, results.size());
        assertEquals("Test Product", results.get(0).getName());
    }

    @Test
    void searchProducts_noMatches_shouldReturnEmpty() {
        when(productRepository.searchProducts("missing")).thenReturn(List.of());

        List<ProductResponse> results = productService.searchProducts("missing");

        assertNotNull(results);
        assertTrue(results.isEmpty());
    }

    @Test
    void updateProduct_whenNotFound_shouldReturnEmpty() {
        ProductRequest req = new ProductRequest();
        req.setName("Nope");

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<ProductResponse> opt = productService.updateProduct(99L, req);

        assertFalse(opt.isPresent());
        verify(productRepository, never()).save(any(Product.class));
    }

    @Test
    void deleteProduct_whenNotFound_shouldReturnFalse() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        boolean result = productService.deleteProduct(99L);

        assertFalse(result);
        verify(productRepository, never()).save(any(Product.class));
    }
}
