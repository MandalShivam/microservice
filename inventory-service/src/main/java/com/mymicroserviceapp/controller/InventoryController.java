package com.mymicroserviceapp.controller;

import com.mymicroserviceapp.dto.InventoryRequest;
import com.mymicroserviceapp.dto.InventoryResponse;
import com.mymicroserviceapp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
        public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }

    @PostMapping("/createInventory")
    @ResponseStatus(HttpStatus.CREATED)
    public String createProduct(@RequestBody InventoryRequest inventoryRequest) {
           return inventoryService.createProduct(inventoryRequest);
    }
}
