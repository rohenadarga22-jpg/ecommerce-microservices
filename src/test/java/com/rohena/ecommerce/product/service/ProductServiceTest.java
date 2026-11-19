
package com.rohena.ecommerce.product.service;

import com.rohena.ecommerce.product.dto.ProductRequest;
import com.rohena.ecommerce.product.dto.ProductResponse;
import com.rohena.ecommerce.product.entity.Product;
import com.rohena.ecommerce.product.repository.ProductRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    // Test 1: Create Product
    @Test
    void createProduct_shouldReturnProductResponse() {

        ProductRequest request = new ProductRequest();
        request.setName("Laptop");
        request.setDescription("Gaming Laptop");
        request.setPrice(new BigDecimal("75000"));
        request.setQuantity(10);

        Product savedProduct = new Product(
                "Laptop",
                "Gaming Laptop",
                new BigDecimal("75000"),
                10
        );

        when(productRepository.save(any(Product.class)))
                .thenReturn(savedProduct);

        ProductResponse response =
                productService.createProduct(request);

        assertNotNull(response);
        assertEquals("Laptop", response.getName());
        assertEquals(new BigDecimal("75000"), response.getPrice());

        verify(productRepository, times(1))
                .save(any(Product.class));
    }

    // Test 2: Get Product By ID
    @Test
    void getProductById_shouldReturnProductResponse() {

        Product product = new Product(
                "Laptop",
                "Gaming Laptop",
                new BigDecimal("75000"),
                10
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        ProductResponse response =
                productService.getProductById(1L);

        assertNotNull(response);
        assertEquals("Laptop", response.getName());

        verify(productRepository).findById(1L);
    }

    // Test 3: Product Not Found
    @Test
    void getProductById_shouldThrowExceptionWhenNotFound() {

        when(productRepository.findById(99L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> productService.getProductById(99L)
        );

        assertEquals(
                "Product not found with id: 99",
                exception.getMessage()
        );
    }

    // Test 4: Get All Products
    @Test
    void getAllProducts_shouldReturnProductList() {

        Product product = new Product(
                "Laptop",
                "Gaming Laptop",
                new BigDecimal("75000"),
                10
        );

        when(productRepository.findAll())
                .thenReturn(List.of(product));

        List<ProductResponse> response =
                productService.getAllProducts();

        assertEquals(1, response.size());
        assertEquals("Laptop", response.get(0).getName());

        verify(productRepository).findAll();
    }

    // Test 5: Update Product
    @Test
    void updateProduct_shouldReturnUpdatedProduct() {

        Product product = new Product(
                "Laptop",
                "Gaming Laptop",
                new BigDecimal("75000"),
                10
        );

        ProductRequest request = new ProductRequest();
        request.setName("Updated Laptop");
        request.setDescription("Updated Description");
        request.setPrice(new BigDecimal("80000"));
        request.setQuantity(15);

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        when(productRepository.save(any(Product.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        ProductResponse response =
                productService.updateProduct(1L, request);

        assertEquals("Updated Laptop", response.getName());
        assertEquals("Updated Description", response.getDescription());
        assertEquals(new BigDecimal("80000"), response.getPrice());
        assertEquals(15, response.getQuantity());

        verify(productRepository).save(any(Product.class));
    }

    // Test 6: Delete Product
    @Test
    void deleteProduct_shouldDeleteProduct() {

        Product product = new Product(
                "Laptop",
                "Gaming Laptop",
                new BigDecimal("75000"),
                10
        );

        when(productRepository.findById(1L))
                .thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository).delete(product);
    }
}
