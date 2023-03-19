package com.programming.wajisobri.notificationservice.controller;

import com.programming.wajisobri.notificationservice.config.RabbitMQConfig;
import com.programming.wajisobri.notificationservice.dto.NotificationResponse;
import com.programming.wajisobri.notificationservice.dto.NotificationsResponse;
import com.programming.wajisobri.notificationservice.model.OrderEvent;
import com.programming.wajisobri.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
    private final NotificationService notificationService;

    @RabbitListener(queues = RabbitMQConfig.ORDER_QUEUE_NAME)
    public void listenOrderEvent(OrderEvent orderEvent) {
        notificationService.listenOrderEvent(orderEvent);
    }

    @GetMapping(value="/notifications")
    @ResponseStatus(HttpStatus.OK)
    public NotificationsResponse getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @GetMapping(value="/notification/{consumerUsername}")
    @ResponseStatus(HttpStatus.OK)
    public NotificationsResponse getAllNotificationByUsername(@PathVariable String consumerUsername) {
        return notificationService.getAllNotificationByUsername(consumerUsername);
    }
}
