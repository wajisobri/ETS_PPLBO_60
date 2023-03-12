package com.programmingtechie.orderservice.dto;

import com.programmingtechie.orderservice.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

public class OrderEventSubscriber {
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(OrderEvent event) {
        System.out.println("Received event: " + event);
    }
}
