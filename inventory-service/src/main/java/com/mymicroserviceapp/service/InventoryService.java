package com.mymicroserviceapp.service;

import com.mymicroserviceapp.dto.InventoryRequest;
import com.mymicroserviceapp.dto.InventoryResponse;
import com.mymicroserviceapp.model.Inventory;
import com.mymicroserviceapp.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    @SneakyThrows // to handle exception // don't use in production its only demo purpose
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                        InventoryResponse.builder().skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).toList();


    }

    public String createProduct(InventoryRequest inventoryRequest) {
        List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(List.of(inventoryRequest.getSkuCode()));
        if(inventories!=null){

        }
          inventoryRepository.save(mapToEntity(inventoryRequest));
          return "product saved successfully";
    }

    Inventory mapToEntity(InventoryRequest inventoryRequest) {
        return Inventory.builder()
                .skuCode(inventoryRequest.getSkuCode())
                .quantity(inventoryRequest.getQuantity())
                .build();
    }
}
