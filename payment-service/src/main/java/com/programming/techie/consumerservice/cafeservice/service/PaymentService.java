package com.programming.techie.consumerservice.cafeservice.service;

import com.programming.techie.consumerservice.cafeservice.dto.OrderResponse;
import com.programming.techie.consumerservice.cafeservice.dto.PaymentResponse;
import com.programming.techie.consumerservice.cafeservice.dto.OrderLineItemsResponse;
import com.programming.techie.consumerservice.cafeservice.model.Payment;
import com.programming.techie.consumerservice.cafeservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final WebClient.Builder webClientBuilder;

    public Payment createPayment(String orderNumber) {
        Payment payment = new Payment();

        // Call order service to get order data
        OrderResponse orderResponse = webClientBuilder.build().get()
                .uri("http://order-service/api/order",
                        uriBuilder -> uriBuilder.queryParam("orderNumber", orderNumber).build())
                .retrieve()
                .bodyToMono(OrderResponse.class)
                .block();

        payment.setTransactionNumber(new Timestamp(System.currentTimeMillis()));
        payment.setOrderNumber(orderNumber);
        payment.setTotalPrice(Arrays.stream(orderResponse.getOrderLineItemsList()).map(x -> x.getPrice().multiply(BigDecimal.valueOf(x.getQuantity()))).reduce(BigDecimal.ZERO, BigDecimal::add));

        paymentRepository.save(payment);

        webClientBuilder.build().put()
                .uri("http://order-service/api/order",
                        uriBuilder -> uriBuilder.queryParam("orderNumber", orderNumber).build())
                .retrieve()
                .bodyToMono(OrderResponse.class)
                .block();

        for(OrderLineItemsResponse i : orderResponse.getOrderLineItemsList()) {
            webClientBuilder.build().put()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder.queryParam("skuCode", i.getSkuCode()).queryParam("quantity", i.getQuantity()).build())
                    .retrieve()
                    .bodyToMono(OrderResponse.class)
                    .block();
        }

        return payment;
    }

    public List<PaymentResponse> getAllPayment() {
        List<Payment> payments = paymentRepository.findAll();

        return payments.stream().map(this::mapToPaymentResponse).toList();
    }

    private PaymentResponse mapToPaymentResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .transactionNumber(payment.getTransactionNumber())
                .orderNumber(payment.getOrderNumber())
                .totalPrice(payment.getTotalPrice())
                .build();
    }
}
