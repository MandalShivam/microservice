package com.mymicroserviceapp.controller;

import com.mymicroserviceapp.dto.OrderRequest;
import com.mymicroserviceapp.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory",fallbackMethod = "fallbackMethod")
    public String placeOrder(@RequestBody OrderRequest orderRequest){
       return orderService.placeOrder(orderRequest);
    }

    // same signature as original method with runtime exception as argument
    public String fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
        return "Oops something went wrong, please order after sometime";
    }

}
