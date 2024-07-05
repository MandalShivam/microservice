package com.mymicroserviceapp.service;

import com.mymicroserviceapp.dto.InventoryResponse;
import com.mymicroserviceapp.dto.OrderLineItemsDto;
import com.mymicroserviceapp.dto.OrderRequest;
import com.mymicroserviceapp.model.Order;
import com.mymicroserviceapp.model.OrderLineItems;
import com.mymicroserviceapp.repository.OrderRepository;
import com.netflix.discovery.converters.Auto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    //private final WebClient.Builder webClientBuilder;

    private RestTemplate restTemplate = new RestTemplate();
    //@Value("${data.uri}")
    //private String inventoryUrl = "http://localhost:8980/api/inventory";
    private String inventoryUrl = "http://inventory-service/api/inventory";

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderLineItems> orderLineItemsList = orderRequest.getOrderLineItemsDtoList().stream()
                .map(this::mapToEntity).toList();
        order.setOrderLineItemsList(orderLineItemsList);

        List<String> skuCodes= orderLineItemsList.stream()
                .map(orderLineItems -> orderLineItems.getSkuCode()).toList();

        //call inventory service, place order if product is in stock

        String url = UriComponentsBuilder.fromHttpUrl(inventoryUrl)
                .queryParam("skuCode",skuCodes).build().toUriString();

        InventoryResponse[] result = restTemplate.getForObject(
                url, InventoryResponse[].class,skuCodes);

           if(Arrays.stream(result).allMatch(InventoryResponse::isInStock)){
                orderRepository.save(order);
                return "Order placed successfully";
            } else {
                throw new IllegalArgumentException("Product is not in stock,please try again later");
            }



}


 // mapping from dto to entity
    private OrderLineItems mapToEntity(OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItems.builder()
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .skuCode(orderLineItemsDto.getSkuCode())
                .build();


    }
    }
