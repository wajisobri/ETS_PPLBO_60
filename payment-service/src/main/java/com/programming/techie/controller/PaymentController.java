package com.programming.techie.controller;

import com.programming.techie.dto.PaymentResponse;
import com.programming.techie.model.Payment;
import com.programming.techie.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PaymentResponse> getAllPayment() {
        return paymentService.getAllPayment();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Payment createPayment(@RequestParam String orderNumber) {
        return paymentService.createPayment(orderNumber);
    }
}
