package com.mymicroserviceapp.service;

import com.mymicroserviceapp.dto.InventoryResponse;
import com.mymicroserviceapp.model.Inventory;
import com.mymicroserviceapp.repository.InventoryRepository;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    // Positive test case
    @Test
    public void testIsInStock_success() throws Exception {
        // Arrange
        List<String> skuCodeList = Arrays.asList("SKU123", "SKU456");
        List<Inventory> mockInventoryList = Arrays.asList(
                new Inventory(1l,"SKU123", 10),
                new Inventory(2l, "SKU456", 5) // Extra item not in skuCodeList (ignored)
        );
        Mockito.when(inventoryRepository.findBySkuCodeIn(skuCodeList)).thenReturn(mockInventoryList);

        // Act
        List<InventoryResponse> actualResponse = inventoryService.isInStock(skuCodeList);

        // Assert
        List<InventoryResponse> expectedResponse = Arrays.asList(
                new InventoryResponse("SKU123", true),
                new InventoryResponse("SKU456", true)
        );
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    // Negative test case - empty skuCode list
    @Test
    void testIsInStock_emptyList() {
        // Arrange
        List<String> skuCodeList = Collections.emptyList();

        // Act
        List<InventoryResponse> actualResponse = inventoryService.isInStock(skuCodeList);

        // Assert
        assertEquals(Collections.emptyList(), actualResponse);
    }

    // Negative test case - repository throws exception
    @Test
    void testIsInStock_repositoryException() {
        // Arrange
        List<String> skuCodeList = Arrays.asList("SKU123");
        Mockito.when(inventoryRepository.findBySkuCodeIn(skuCodeList)).thenThrow(new RuntimeException("Repository error"));

        // Act & Assert (expected exception)
        assertThatThrownBy(() -> inventoryService.isInStock(skuCodeList))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Repository error");
    }
}

