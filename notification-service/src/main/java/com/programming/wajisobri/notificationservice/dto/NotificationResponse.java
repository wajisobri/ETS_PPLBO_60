package com.programming.wajisobri.notificationservice.dto;

import com.programming.wajisobri.notificationservice.model.NotificationEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private Integer code;
    private String message;
    private NotificationEvent data;
}
