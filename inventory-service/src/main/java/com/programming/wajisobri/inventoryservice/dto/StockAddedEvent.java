package com.programming.wajisobri.inventoryservice.dto;

import com.programming.wajisobri.inventoryservice.model.Inventory;
import com.programming.wajisobri.inventoryservice.model.StockEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockAddedEvent implements StockEvent {
    private Inventory inventoryDetail;
}
