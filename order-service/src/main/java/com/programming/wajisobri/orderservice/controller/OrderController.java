package com.programming.wajisobri.orderservice.controller;

import com.programming.wajisobri.orderservice.dto.OrderResponse;
import com.programming.wajisobri.orderservice.dto.OrderRequest;
import com.programming.wajisobri.orderservice.dto.OrdersResponse;
import com.programming.wajisobri.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
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

    @GetMapping(value="/order")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponse getOrderByOrderNumber(@RequestParam String orderNumber) {
        return orderService.getOrderByOrderNumber(orderNumber);
    }

//    @PutMapping(value="/order")
//    @ResponseStatus(HttpStatus.OK)
//    public Order changeStatusOrderByOrderNumber(@RequestParam String orderNumber) {
//        return orderService.changeStatusOrderByOrderNumber(orderNumber);
//    }
}
