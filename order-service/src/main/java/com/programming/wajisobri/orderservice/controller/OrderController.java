package com.programming.wajisobri.orderservice.controller;

import com.programming.wajisobri.orderservice.config.RabbitMQConfig;
import com.programming.wajisobri.orderservice.dto.OrderResponse;
import com.programming.wajisobri.orderservice.dto.OrderRequest;
import com.programming.wajisobri.orderservice.dto.OrdersResponse;
import com.programming.wajisobri.orderservice.model.PaymentEvent;
import com.programming.wajisobri.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value="/order")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse placeOrder(@RequestParam String username, @RequestParam String cafeId, @RequestBody OrderRequest orderRequest) {
        return orderService.placeOrder(username, cafeId, orderRequest);
    }

    @GetMapping(value="/orders")
    @ResponseStatus(HttpStatus.OK)
    public OrdersResponse getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping(value="/order/{orderNumber}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse getOrderByOrderNumber(@PathVariable String orderNumber) {
        return orderService.getOrderByOrderNumber(orderNumber);
    }

    @RabbitListener(queues = RabbitMQConfig.PAYMENT_QUEUE_NAME)
    public void receivePaymentRequest(PaymentEvent paymentEvent) {
        orderService.receivePaymentResponse(paymentEvent);
    }

    @GetMapping(value="/order/cancel/{orderNumber}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse cancelOrderByOrderNumber(@PathVariable String orderNumber) {
        return orderService.cancelOrder(orderNumber);
    }

//    @PutMapping(value="/order")
//    @ResponseStatus(HttpStatus.OK)
//    public Order changeStatusOrderByOrderNumber(@RequestParam String orderNumber) {
//        return orderService.changeStatusOrderByOrderNumber(orderNumber);
//    }
}
