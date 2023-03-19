package com.programming.wajisobri.notificationservice.service;

import com.programming.wajisobri.notificationservice.dto.NotificationResponse;
import com.programming.wajisobri.notificationservice.dto.NotificationsResponse;
import com.programming.wajisobri.notificationservice.model.NotificationEvent;
import com.programming.wajisobri.notificationservice.model.OrderEvent;
import com.programming.wajisobri.notificationservice.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public void listenOrderEvent(OrderEvent orderEvent) {
        log.info("Order message received");
        String message = "";
        if(orderEvent.getEventType().toString() == "Order_Created") {
            message = "Order created by with order number " + orderEvent.getEventData().get("order_number");
        } else if(orderEvent.getEventType().toString() == "Order_Cancelled") {
            message = "Order cancelled by with order number " + orderEvent.getEventData().get("order_number");
        }

        NotificationEvent notificationEvent = NotificationEvent.builder()
                .id(UUID.randomUUID().toString())
                .message(message)
                .usernameRecipient(orderEvent.getEventData().get("username").toString())
                .createdAt(LocalDateTime.now())
                .sent(false)
                .build();

        pushNotification(notificationEvent);
    }

    public void pushNotification(NotificationEvent notificationEvent) {
        log.info("Notification has been sent to " + notificationEvent.getUsernameRecipient());
        log.info("Message: " + notificationEvent.getMessage());
    }

    public NotificationsResponse getAllNotifications() {
        try {
            List<NotificationEvent> notifications = notificationRepository.findAll();

            notifications = notifications.stream().map(this::mapToNotificationResponse).toList();
            return NotificationsResponse.builder()
                    .code(200)
                    .message("Notifications retrieved")
                    .data(notifications)
                    .build();
        } catch (DataAccessException e) {
            return NotificationsResponse.builder()
                    .code(500)
                    .message("An error occurred while accessing the database")
                    .build();
        } catch (Exception e) {
            return NotificationsResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
    }

    private NotificationEvent mapToNotificationResponse(NotificationEvent notification) {
        return NotificationEvent.builder()
                .id(notification.getId())
                .message(notification.getMessage())
                .usernameRecipient(notification.getUsernameRecipient())
                .createdAt(notification.getCreatedAt())
                .sent(notification.getSent())
                .build();
    }

    public NotificationsResponse getAllNotificationByUsername(String consumerUsername) {
        try {
            List<NotificationEvent> notificationEvents = notificationRepository.findByUsernameRecipient(consumerUsername);
            return NotificationsResponse.builder()
                    .code(200)
                    .message("Notification for consumer " + consumerUsername + " retrieved")
                    .data(notificationEvents)
                    .build();
        } catch (NoSuchElementException e) {
            return NotificationsResponse.builder()
                    .code(404)
                    .message(e.getMessage())
                    .build();
        } catch (DataAccessException e) {
            return NotificationsResponse.builder()
                    .code(500)
                    .message(e.getMessage())
                    .build();
        }
    }
}
