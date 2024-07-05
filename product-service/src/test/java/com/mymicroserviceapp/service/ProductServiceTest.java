package com.mymicroserviceapp.service;

import com.mymicroserviceapp.dto.ProductRequest;
import com.mymicroserviceapp.dto.ProductResponse;
import com.mymicroserviceapp.model.Product;
import com.mymicroserviceapp.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    // Positive test case - createProduct success
    @Test
    void testCreateProduct_success() {
        // Arrange
        ProductRequest productRequest = new ProductRequest("Product Name", "Description", new BigDecimal(10.99));
        Product expectedProduct = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(expectedProduct);

        productService.createProduct(productRequest);
        assertEquals("Product Name",expectedProduct.getName());

    }

    // Negative test case - createProduct with null request body (exception expected)
    @Test // Expect specific exception
    void testCreateProduct_nullRequest() throws Exception {
        // Arrange (no arrangement needed for null request)

        // Act & Assert (expected exception)
        assertThrows(IllegalArgumentException.class, () -> productService.createProduct(null));
    }

    // Positive test case - getProducts success
    @Test
    void testGetProducts_success() throws Exception {
        // Arrange
        List<Product> mockProducts = Arrays.asList(
                new Product(1, "Product 1", "Description 1", new BigDecimal(12.34)),
                new Product(2, "Product 2", "Description 2", new BigDecimal(56.78))
        );
        Mockito.when(productRepository.findAll()).thenReturn(mockProducts);

        // Act
        List<ProductResponse> actualResponse = productService.getProducts();

        // Assert
        List<ProductResponse> expectedResponse = mockProducts.stream()
                .map(this::mapToProductResponse) // Use a separate method if needed
                .collect(Collectors.toList());
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    // Helper method to map Product to ProductResponse (optional)
    private ProductResponse mapToProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getDescription(), product.getPrice());
    }
}
