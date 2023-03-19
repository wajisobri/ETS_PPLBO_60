package com.programming.wajisobri.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashMap;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    public enum EventType {
        Order_Created, Order_Unpaid, Order_Paid, Order_Preparing, Order_Delivering, Order_Finished, Order_Cancelled
    }
    private String eventId;
    private EventType eventType;
    private HashMap<String, Object> eventData = new HashMap<>();
    private LocalDateTime eventTime;
}
