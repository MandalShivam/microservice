package com.mymicroserviceapp.controller;

import com.mymicroserviceapp.dto.ProductRequest;
import com.mymicroserviceapp.dto.ProductResponse;
import com.mymicroserviceapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody  ProductRequest productRequest){

        productService.createProduct(productRequest);
    }

    public List<ProductResponse> getProducts() {
        return productService.getProducts();
    }
}
