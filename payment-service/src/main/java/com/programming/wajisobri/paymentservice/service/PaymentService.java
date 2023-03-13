package com.programming.wajisobri.paymentservice.service;

import com.programming.wajisobri.paymentservice.config.RabbitMQConfig;
import com.programming.wajisobri.paymentservice.dto.OrderEvent;
import com.programming.wajisobri.paymentservice.dto.PaymentResponse;
import com.programming.wajisobri.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public void receivePaymentRequest(OrderEvent orderEvent) {
        logger.info("Received payment request for order id: " + orderEvent.getEventData().get("order_number"));

        // Process the payment
        boolean paymentSuccess = processPayment(orderEvent);

        // Send payment response to the queue
        PaymentResponse paymentResponse = new PaymentResponse(orderEvent.getEventData().get("order_number").toString(), paymentSuccess);

        logger.info("Sent payment response for order id: " + orderEvent.getEventData().get("order_number"));
    }

    private boolean processPayment(OrderEvent orderEvent) {
        // Payment processing logic goes here
        return true;
    }
}
