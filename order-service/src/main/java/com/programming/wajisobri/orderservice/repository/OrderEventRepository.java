package com.programming.wajisobri.orderservice.repository;

import com.programming.wajisobri.orderservice.model.OrderEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface OrderEventRepository extends MongoRepository<OrderEvent, String> {
    List<OrderEvent> findByEventType(String eventType);
}
