package com.mymicroserviceapp.service;

import com.mymicroserviceapp.dto.InventoryResponse;
import com.mymicroserviceapp.dto.OrderLineItemsDto;
import com.mymicroserviceapp.dto.OrderRequest;
import com.mymicroserviceapp.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testPlaceOrder_Success() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest();
        List<String> skuCodes = Arrays.asList("SKU123");

        String url = UriComponentsBuilder.fromHttpUrl("http://inventory-service/api/inventory")
                .queryParam("skuCode",skuCodes).build().toUriString();
        InventoryResponse[] inventoryResponses = {new InventoryResponse("SKU123", true)};

        orderRequest.setOrderLineItemsDtoList(List.of(new OrderLineItemsDto(1l,"SKU123",new BigDecimal(2.1), 2)));
        when(restTemplate.getForObject(url, InventoryResponse[].class,skuCodes)).thenReturn(inventoryResponses);

//        when(orderRepository.save(any(Order.class))).thenReturn(new Order());


        // Act
        String result = orderService.placeOrder(orderRequest);

        // Assert
        assertEquals("Order placed successfully", result);
//        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    public void testPlaceOrder_NotInStock() {
        // Arrange
        OrderRequest orderRequest = new OrderRequest();
        List<String> skuCodes = Arrays.asList("SKU123");
        String url = UriComponentsBuilder.fromHttpUrl("http://inventory-service/api/inventory")
                .queryParam("skuCode",skuCodes).build().toUriString();

        orderRequest.setOrderLineItemsDtoList(List.of(new OrderLineItemsDto(2l,"SKU123",new BigDecimal(2), 2)));
        InventoryResponse[] inventoryResponses = {new InventoryResponse("SKU123", false)};
        when(restTemplate.getForObject(url, InventoryResponse[].class,skuCodes)).thenReturn(inventoryResponses);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> orderService.placeOrder(orderRequest));
    }
}
