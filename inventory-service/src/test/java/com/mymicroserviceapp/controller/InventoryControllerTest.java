package com.mymicroserviceapp.controller;

import com.mymicroserviceapp.dto.InventoryResponse;
import com.mymicroserviceapp.service.InventoryService;
import jakarta.ws.rs.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class InventoryControllerTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    // Positive test case
    @Test
    public void testIsInStock_success() throws Exception {
        // Arrange
        List<String> skuCodeList = Arrays.asList("SKU123", "SKU456");
        List<InventoryResponse> expectedResponse = Arrays.asList(
                new InventoryResponse("SKU123", true),
                new InventoryResponse("SKU456", false)
        );
        Mockito.when(inventoryService.isInStock(skuCodeList)).thenReturn(expectedResponse);

        // Act
        List<InventoryResponse> actualResponse = inventoryController.isInStock(skuCodeList);

        // Assert
        assertThat(actualResponse).isEqualTo(expectedResponse);
        Mockito.verify(inventoryService).isInStock(skuCodeList);
    }

    // Negative test case - empty skuCode list
    @Test
    public void testIsInStock_emptyList() {
        // Arrange
        List<String> skuCodeList = Collections.emptyList();

        // Act & Assert (expected null response or exception - adjust based on implementation)
        List<InventoryResponse> actualResponse = inventoryController.isInStock(skuCodeList);
        if (inventoryController.isInStock(skuCodeList) == null) {

            assertThrows(BadRequestException.class, () -> inventoryController.isInStock(skuCodeList));

        } else {
            assertEquals(Collections.emptyList(), actualResponse, "Expected empty response list");
        }
       // Mockito.verifyNoInteractions(inventoryService); // No service call for empty list
    }

    // Negative test case - service throws exception
    @Test
    public void testIsInStock_serviceException() throws Exception {
        // Arrange
        List<String> skuCodeList = Arrays.asList("SKU123");
        Mockito.when(inventoryService.isInStock(skuCodeList)).thenThrow(new RuntimeException("Inventory service error"));

        // Act & Assert (expected runtime exception)
        assertThrows(RuntimeException.class, () -> inventoryController.isInStock(skuCodeList));
        Mockito.verify(inventoryService).isInStock(skuCodeList);
    }
}