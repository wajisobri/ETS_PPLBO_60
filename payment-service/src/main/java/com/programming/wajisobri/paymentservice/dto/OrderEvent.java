package com.programming.wajisobri.paymentservice.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.HashMap;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
@ToString
public class OrderEvent {
    public enum EventType {
        Order_Created, Order_Unpaid, Order_Paid, Order_Delivered, Order_Finished, Order_Cancelled
    }
    private String eventId;
    private EventType eventType;
    private HashMap<String, Object> eventData = new HashMap<>();
    private LocalDateTime eventTime;
}