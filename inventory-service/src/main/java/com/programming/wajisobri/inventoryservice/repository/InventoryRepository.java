package com.programming.wajisobri.inventoryservice.repository;

import com.programming.wajisobri.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    List<Inventory> findBySkuCodeIn(List<String> skuCode);

    Inventory findBySkuCode(String skuCode);
}
