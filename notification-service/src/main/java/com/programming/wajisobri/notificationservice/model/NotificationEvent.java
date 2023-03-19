package com.programming.wajisobri.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(value = "notification_events")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class NotificationEvent {
    @Id
    private String id;
    private String message;
    private String usernameRecipient;
    private LocalDateTime createdAt;
    private Boolean sent;
}
