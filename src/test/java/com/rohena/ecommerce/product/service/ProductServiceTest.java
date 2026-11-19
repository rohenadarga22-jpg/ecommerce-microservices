
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

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
}
