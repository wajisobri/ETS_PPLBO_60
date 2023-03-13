package com.programming.wajisobri.inventoryservice.service;

import com.programming.wajisobri.inventoryservice.model.Inventory;
import com.programming.wajisobri.inventoryservice.repository.InventoryRepository;
import com.programming.wajisobri.inventoryservice.dto.InventoryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .quantity(inventory.getQuantity())
                                .build()
                ).toList();
    }

    @Transactional(readOnly = true)
    public Inventory getInventory(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode);
    }

    public Inventory updateStock(String skuCode, Integer quantity) {
        Inventory inv = inventoryRepository.findBySkuCode(skuCode);
        inv.setQuantity(inv.getQuantity() - quantity);
        return inventoryRepository.save(inv);
    }

    public Inventory addStock(String skuCode, Integer quantity) {
        Inventory inv = inventoryRepository.findBySkuCode(skuCode);
        inv.setQuantity(inv.getQuantity() + quantity);
        return inventoryRepository.save(inv);
    }


    public Inventory removeStock(String skuCode, Integer quantity) {
        Inventory inv = inventoryRepository.findBySkuCode(skuCode);
        inv.setQuantity(inv.getQuantity() - quantity);
        return inventoryRepository.save(inv);
    }

    public List<Inventory> getAllStock() {
        List<Inventory> inventories = inventoryRepository.findAll();
        return inventories;
    }
}
