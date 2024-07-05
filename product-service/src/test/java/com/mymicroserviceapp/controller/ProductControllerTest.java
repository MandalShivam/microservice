package com.mymicroserviceapp.controller;

import com.mymicroserviceapp.dto.ProductRequest;
import com.mymicroserviceapp.dto.ProductResponse;
import com.mymicroserviceapp.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    // Positive test case - create product success
    @Test
    void testCreateProduct_success() throws Exception {
        // Arrange
        ProductRequest productRequest = new
                ProductRequest("Product Name",
                            "Description",new BigDecimal("123.45"));

        // Act
        productController.createProduct(productRequest);

        // Assert (verify service call)
        Mockito.verify(productService).createProduct(productRequest);
    }

    // Positive test case - getProducts success
    @Test
    void testGetProducts_success() throws Exception {
        // Arrange
        List<ProductResponse> mockResponse = Arrays.asList(
                new ProductResponse(1, "Product 1", "Description 1",new BigDecimal("345.56")),
                new ProductResponse(2, "Product 2", "Description 2",new BigDecimal("123.45"))
        );
        Mockito.when(productService.getProducts()).thenReturn(mockResponse);

        // Act
        List<ProductResponse> actualResponse = productController.getProducts();

        // Assert
        assertThat(actualResponse).isEqualTo(mockResponse);
    }

    // Negative test case - getProducts throws exception (exception propagation)
    @Test
    void testGetProducts_serviceException() throws Exception {
        // Arrange
        Mockito.when(productService.getProducts()).thenThrow(new RuntimeException("Service error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> productController.getProducts());
    }
}