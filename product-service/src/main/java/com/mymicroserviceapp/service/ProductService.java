package com.mymicroserviceapp.service;

import com.mymicroserviceapp.dto.ProductRequest;
import com.mymicroserviceapp.dto.ProductResponse;
import com.mymicroserviceapp.model.Product;
import com.mymicroserviceapp.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {


    private final RestTemplate restTemplate;
    private final ProductRepository productRepository; // using @RequiredArgsConstructor we are
    // getting productRepository object from spring container (it is working as constructor injection)

    @Transactional
    public void createProduct(ProductRequest productRequest){
        if(productRequest==null) {
            throw new IllegalArgumentException("parameter should not be null");
        } else {
            Product product = Product.builder()
                    .description(productRequest.getDescription())
                    .price(productRequest.getPrice())
                    .name(productRequest.getName())
                    .build();
            createInventory(productRequest);
            productRepository.save(product);
            log.info("product {} is created", product.getId());
        }
    }

    public List<ProductResponse> getProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    private void createInventory(ProductRequest productRequest) {
        String skuCode = productRequest.getName();
        Integer quantity=0;
        String url = "http://localhost:8980/api/inventory/createInventory";
        String response = restTemplate.postForObject(url,)
    }

    private ProductResponse mapToProductResponse(Product product) {
        return  ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
