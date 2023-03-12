package com.programming.techie.paymentservice.controller;

import com.programming.techie.paymentservice.config.RabbitMQConfig;
import com.programming.techie.paymentservice.dto.OrderEvent;
import com.programming.techie.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receivePaymentRequest(OrderEvent orderEvent) {
        paymentService.receivePaymentRequest(orderEvent);
    }
}
