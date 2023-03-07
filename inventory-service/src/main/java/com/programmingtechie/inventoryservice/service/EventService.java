package com.programmingtechie.inventoryservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.inventoryservice.dto.StockRemovedEvent;
import com.programmingtechie.inventoryservice.model.EventStore;
import com.programmingtechie.inventoryservice.dto.StockAddedEvent;
import com.programmingtechie.inventoryservice.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository repo;

    public EventStore addEvent(StockAddedEvent event) throws JsonProcessingException {
        EventStore eventStore = new EventStore();
        eventStore.setEventData(new ObjectMapper().writeValueAsString(event.getInventoryDetail()));
        eventStore.setEventType("INVENTORY_STOCK_ADDED");
        eventStore.setEntityId(event.getInventoryDetail().getSkuCode());
        eventStore.setEventTime(LocalDateTime.now());
        repo.save(eventStore);
        return eventStore;
    }

    public EventStore addEvent(StockRemovedEvent event) throws JsonProcessingException {
        EventStore eventStore = new EventStore();
        eventStore.setEventData(new ObjectMapper().writeValueAsString(event.getInventoryDetail()));
        eventStore.setEventType("INVENTORY_STOCK_REMOVED");
        eventStore.setEntityId(event.getInventoryDetail().getSkuCode());
        eventStore.setEventTime(LocalDateTime.now());
        repo.save(eventStore);
        return eventStore;
    }

    public Iterable<EventStore> fetchAllEvents(String name) {
        return repo.findByEntityId(name);
    }

    public Iterable<EventStore> fetchAllEventsTillDate(String name, LocalDateTime date) {
        return repo.findByEntityIdAndEventTimeLessThanEqual(name, date);
    }
}
