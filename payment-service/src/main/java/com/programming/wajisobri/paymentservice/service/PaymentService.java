package com.programming.wajisobri.paymentservice.service;

import com.programming.wajisobri.paymentservice.config.RabbitMQConfig;
import com.programming.wajisobri.paymentservice.dto.OrderCustomResponse;
import com.programming.wajisobri.paymentservice.dto.OrderResponse;
import com.programming.wajisobri.paymentservice.model.OrderEvent;
import com.programming.wajisobri.paymentservice.model.PaymentEvent;
import com.programming.wajisobri.paymentservice.dto.PaymentRequest;
import com.programming.wajisobri.paymentservice.dto.PaymentResponse;
import com.programming.wajisobri.paymentservice.model.Payment;
import com.programming.wajisobri.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.criterion.Order;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final WebClient.Builder webClientBuilder;

    @Autowired
    private final AmqpTemplate amqpTemplate;

    public void receivePaymentRequest(OrderEvent orderEvent) {
        log.info("Received payment request for order number: " + orderEvent.getEventData().get("order_number"));

        if(orderEvent.getEventType().toString() == "Order_Created") {
            // Process the payment
            boolean paymentSuccess = processPayment(orderEvent);
            if(paymentSuccess) {
                log.info("Sent payment response for order number: " + orderEvent.getEventData().get("order_number"));
            } else {
                PaymentEvent paymentEvent = new PaymentEvent();
                paymentEvent.setEventId(UUID.randomUUID().toString());
                paymentEvent.setEventType(PaymentEvent.EventType.Payment_Cancelled);
                HashMap<String, Object> eventData = new HashMap<>();
                eventData.put("order_number", orderEvent.getEventData().get("order_number"));
                paymentEvent.setEventData(eventData);
                paymentEvent.setEventTime(LocalDateTime.now());
            }
        } else if(orderEvent.getEventType().toString() == "Order_Cancelled") {
            // Cancel payment
            Payment retrievedPayment = paymentRepository.findByOrderNumber(orderEvent.getEventData().get("order_number").toString());
            retrievedPayment.setPaymentStatus(Payment.PaymentStatus.Cancelled);
            try {
                Payment cancelledPayment = paymentRepository.save(retrievedPayment);

                // Generate cancelled invoice
                log.info("Payment with order number " + orderEvent.getEventData().get("order_number").toString() + " has been cancelled");
            } catch (DataAccessException e) {
                log.info(e.getMessage());
            } catch (Exception e) {
                log.info(e.getMessage());
            }
        }
    }

    private boolean processPayment(OrderEvent orderEvent) {
        // Payment processing logic goes here
        Payment newPayment = new Payment();
        newPayment.setPaymentNumber(UUID.randomUUID().toString());
        newPayment.setOrderNumber(orderEvent.getEventData().get("order_number").toString());
        newPayment.setPaymentStatus(Payment.PaymentStatus.Unpaid);
        newPayment.setAmount(BigDecimal.valueOf(Double.parseDouble(orderEvent.getEventData().get("total_price").toString())));

        try {
            // Save payment to database
            Payment savedPayment = paymentRepository.save(newPayment);

            // Send payment response to the queue
            PaymentEvent paymentEvent = new PaymentEvent();
            paymentEvent.setEventId(UUID.randomUUID().toString());
            paymentEvent.setEventType(PaymentEvent.EventType.Payment_Created);
            HashMap<String, Object> eventData = new HashMap<>();
            eventData.put("payment_number", savedPayment.getPaymentNumber());
            eventData.put("order_number", savedPayment.getOrderNumber());
            eventData.put("amount", savedPayment.getAmount());
            eventData.put("payment_status", savedPayment.getPaymentStatus());
            paymentEvent.setEventData(eventData);
            paymentEvent.setEventTime(LocalDateTime.now());

            amqpTemplate.convertAndSend(RabbitMQConfig.PAYMENT_EXCHANGE_NAME, RabbitMQConfig.PAYMENT_ROUTING_KEY, paymentEvent);

            // Generate invoice
            log.info("Invoice has been generated");
            log.info("Payment processed, please complete the payment immediately");
            log.info(savedPayment.getPaymentNumber().toString());
            return true;
        } catch (DataAccessException e) {
            log.info(e.getMessage());
            return false;
        } catch (Exception e) {
            log.info(e.getMessage());
            return false;
        }
    }

    public PaymentResponse payInvoice(PaymentRequest paymentRequest) {
        Payment retrievedPayment = paymentRepository.findByOrderNumber(paymentRequest.getOrderNumber());

        retrievedPayment.setPaymentStatus(Payment.PaymentStatus.Paid);
        retrievedPayment.setPaymentMethod(Payment.PaymentMethod.valueOf(paymentRequest.getPaymentMethod().toString()));
        retrievedPayment.setPaymentTime(LocalDateTime.now());

        try {
            Payment updatedPayment = paymentRepository.save(retrievedPayment);

            // Send payment response to the queue
            PaymentEvent paymentEvent = new PaymentEvent();
            paymentEvent.setEventId(UUID.randomUUID().toString());
            paymentEvent.setEventType(PaymentEvent.EventType.Payment_Finished);
            HashMap<String, Object> eventData = new HashMap<>();
            eventData.put("payment_number", updatedPayment.getPaymentNumber());
            eventData.put("order_number", updatedPayment.getOrderNumber());
            eventData.put("amount", updatedPayment.getAmount());
            eventData.put("payment_method", updatedPayment.getPaymentMethod());
            eventData.put("payment_status", updatedPayment.getPaymentStatus());
            eventData.put("payment_time", updatedPayment.getPaymentTime());
            paymentEvent.setEventData(eventData);
            paymentEvent.setEventTime(LocalDateTime.now());

            amqpTemplate.convertAndSend(RabbitMQConfig.PAYMENT_EXCHANGE_NAME, RabbitMQConfig.PAYMENT_ROUTING_KEY, paymentEvent);

            return PaymentResponse.builder()
                    .code(200)
                    .message("Orders retrieved")
                    .data(updatedPayment)
                    .build();
        } catch (DataAccessException e) {
            return PaymentResponse.builder()
                    .code(500)
                    .message("An error occurred while accessing the database")
                    .build();
        } catch (Exception e) {
            return PaymentResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
    }
}
