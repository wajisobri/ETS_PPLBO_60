package com.programming.wajisobri.orderservice.dto;

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
public class PaymentEvent {
    public enum EventType {
        Payment_Created, Payment_Finished, Payment_Cancelled
    }
    private String eventId;
    private EventType eventType;
    private HashMap<String, Object> eventData = new HashMap<>();
    private LocalDateTime eventTime;
}
