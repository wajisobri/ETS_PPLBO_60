package com.programming.wajisobri.inventoryservice.repository;

import com.programming.wajisobri.inventoryservice.model.EventStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<EventStore, Long>  {
    Iterable<EventStore> findByEntityId(String entityId);

    Iterable<EventStore> findByEntityIdAndEventTimeLessThanEqual(String entityId, LocalDateTime date);
}
