package com.programmingtechie.inventoryservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.inventoryservice.dto.InventoryDto;
import com.programmingtechie.inventoryservice.dto.InventoryResponse;
import com.programmingtechie.inventoryservice.dto.StockAddedEvent;
import com.programmingtechie.inventoryservice.dto.StockRemovedEvent;
import com.programmingtechie.inventoryservice.model.EventStore;
import com.programmingtechie.inventoryservice.model.Inventory;
import com.programmingtechie.inventoryservice.service.EventService;
import com.programmingtechie.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final EventService eventService;

    // http://localhost:8083/api/inventory/iphone-13,iphone13-red

    // http://localhost:8083/api/inventory?skuCode=iphone-13&skuCode=iphone13-red
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public Inventory updateStock(@RequestParam String skuCode, Integer quantity) {
        return inventoryService.updateStock(skuCode, quantity);
    }

    // Inventory Stock Method
    @GetMapping(value="/stock")
    @ResponseStatus(HttpStatus.OK)
    public List<Inventory> getAllStock() {
        return inventoryService.getAllStock();
    }

    @PostMapping(value="/stock")
    @ResponseStatus(HttpStatus.OK)
    public Inventory addStock(@RequestParam String skuCode, Integer quantity) {
        return inventoryService.addStock(skuCode, quantity);
    }

    @DeleteMapping(value="/stock")
    @ResponseStatus(HttpStatus.OK)
    public Inventory removeStock(@RequestParam String skuCode, Integer quantity) {
        return inventoryService.removeStock(skuCode, quantity);
    }

    // Inventory Stock Event Method
    @GetMapping(value="/stock/event")
    @ResponseStatus(HttpStatus.OK)
    public Inventory getInventory(@RequestParam String skuCode) throws JsonProcessingException {
        Iterable<EventStore> events = eventService.fetchAllEvents(skuCode);

        Inventory currentStock = new Inventory();
        currentStock.setSkuCode(skuCode);
        currentStock.setQuantity(0);

        for (EventStore event : events) {
            Inventory stock = new ObjectMapper().readValue(event.getEventData(), Inventory.class);
            currentStock.setId(stock.getId());
            if (event.getEventType().equals("INVENTORY_STOCK_ADDED")) {
                currentStock.setQuantity(currentStock.getQuantity() + stock.getQuantity());
            } else if (event.getEventType().equals("INVENTORY_STOCK_REMOVED")) {
                currentStock.setQuantity(currentStock.getQuantity() - stock.getQuantity());
            }
        }

        return currentStock;
    }

    @GetMapping(value="/stock/event/list")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<EventStore> getEvent(@RequestParam String skuCode) throws JsonProcessingException {
        Iterable<EventStore> events = eventService.fetchAllEvents(skuCode);
        return events;
    }

    @GetMapping(value="/stock/history")
    @ResponseStatus(HttpStatus.OK)
    public Inventory getStockUntilDate(@RequestParam String date,@RequestParam String skuCode) throws JsonProcessingException {
        String[] dateArray = date.split("-");
        LocalDateTime dateTill = LocalDate.of(Integer.parseInt(dateArray[0]), Integer.parseInt(dateArray[1]), Integer.parseInt(dateArray[2])).atTime(23, 59);

        Iterable<EventStore> events = eventService.fetchAllEventsTillDate(skuCode,dateTill);

        Inventory currentStock = new Inventory();
        currentStock.setSkuCode(skuCode);
        currentStock.setQuantity(0);

        for (EventStore event : events) {
            Inventory stock = new ObjectMapper().readValue(event.getEventData(), Inventory.class);
            currentStock.setId(stock.getId());
            if (event.getEventType().equals("INVENTORY_STOCK_ADDED")) {
                currentStock.setQuantity(currentStock.getQuantity() + stock.getQuantity());
            } else if (event.getEventType().equals("INVENTORY_STOCK_REMOVED")) {
                currentStock.setQuantity(currentStock.getQuantity() - stock.getQuantity());
            }
        }

        return currentStock;
    }

    @PostMapping(value="/stock/event")
    @ResponseStatus(HttpStatus.OK)
    public EventStore addStockWithEvent(@RequestParam String skuCode, Integer quantity) throws JsonProcessingException {
        Inventory inventory = new Inventory();
        inventory.setId(inventoryService.getInventory(skuCode).getId());
        inventory.setSkuCode(skuCode);
        inventory.setQuantity(quantity);

        StockAddedEvent event = StockAddedEvent.builder().inventoryDetail(inventory).build();
        return eventService.addEvent(event);
    }

    @DeleteMapping(value="/stock/event")
    @ResponseStatus(HttpStatus.OK)
    public EventStore removeStockWithEvent(@RequestParam String skuCode, Integer quantity) throws JsonProcessingException {
        Inventory inventory = new Inventory();
        inventory.setId(inventoryService.getInventory(skuCode).getId());
        inventory.setSkuCode(skuCode);
        inventory.setQuantity(quantity);

        StockRemovedEvent event = StockRemovedEvent.builder().inventoryDetail(inventory).build();
        return eventService.addEvent(event);
    }
}

