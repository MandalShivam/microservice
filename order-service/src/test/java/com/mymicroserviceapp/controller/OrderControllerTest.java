package com.mymicroserviceapp.controller;

import com.mymicroserviceapp.dto.OrderRequest;
import com.mymicroserviceapp.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @Test
    void placeOrder_shouldReturnCreatedStatus() {
        // Arrange
       OrderRequest orderRequest = new OrderRequest();
       String expectedResponse = "Order placed successfully";
       Mockito.when(orderService.placeOrder(orderRequest)).thenReturn(expectedResponse);

       String response = orderController.placeOrder(orderRequest);

       assertEquals(response,expectedResponse);
       verify(orderService,times(1)).placeOrder(orderRequest);

    }

    @Test
    public void testPlaceOrder_fallback() throws Exception {
        // Arrange
       OrderRequest orderRequest = new OrderRequest();
       String response = orderController.fallbackMethod(orderRequest,new RuntimeException());

       assertEquals("Oops something went wrong, please order after sometime",response);

    }
}
