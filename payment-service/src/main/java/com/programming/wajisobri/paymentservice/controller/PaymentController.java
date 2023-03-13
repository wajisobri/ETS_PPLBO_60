package com.programming.wajisobri.paymentservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.programming.wajisobri.paymentservice.config.RabbitMQConfig;
import com.programming.wajisobri.paymentservice.dto.OrderEvent;
import com.programming.wajisobri.paymentservice.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {
    private final PaymentService paymentService;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receivePaymentRequest(OrderEvent orderEvent) {
        // log.info(orderEvent.getEventData().toString());
        paymentService.receivePaymentRequest(orderEvent);
    }
}
