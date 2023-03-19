package com.programming.wajisobri.notificationservice.dto;

import com.programming.wajisobri.notificationservice.model.NotificationEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationsResponse {
    private Integer code;
    private String message;
    private List<NotificationEvent> data;
}