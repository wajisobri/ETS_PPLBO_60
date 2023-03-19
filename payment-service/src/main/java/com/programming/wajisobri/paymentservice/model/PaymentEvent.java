package com.programming.wajisobri.paymentservice.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.HashMap;

@Document(value = "payment_events")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PaymentEvent {
    public enum EventType {
        Payment_Created, Payment_Finished, Payment_Cancelled
    }
    @Id
    private String eventId;
    private EventType eventType;
    private HashMap<String, Object> eventData = new HashMap<>();
    private LocalDateTime eventTime;
}
