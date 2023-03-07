package com.programmingtechie.inventoryservice.dto;

import com.programmingtechie.inventoryservice.model.Inventory;
import com.programmingtechie.inventoryservice.model.StockEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StockRemovedEvent implements StockEvent {
    private Inventory inventoryDetail;
}
