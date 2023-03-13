package com.programming.wajisobri.consumerservice.controller;

import com.programming.wajisobri.consumerservice.dto.ConsumerRequest;
import com.programming.wajisobri.consumerservice.dto.ConsumerResponse;
import com.programming.wajisobri.consumerservice.dto.ConsumersResponse;
import com.programming.wajisobri.consumerservice.service.ConsumerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ConsumerController {
    private final ConsumerService consumerService;

    @GetMapping(value="/consumers")
    @ResponseStatus(HttpStatus.OK)
    public ConsumersResponse getAllConsumers() {
        return consumerService.getAllConsumer();
    }

    @GetMapping(value="/consumer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ConsumerResponse getConsumer(@PathVariable Long id) {
        return consumerService.getConsumer(id);
    }

    @PostMapping(value="/consumer")
    @ResponseStatus(HttpStatus.CREATED)
    public ConsumerResponse createConsumer(@RequestBody ConsumerRequest consumerRequest) {
        return consumerService.createConsumer(consumerRequest);
    }

    @PutMapping(value="/consumer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ConsumerResponse updateConsumer(@PathVariable Long id, @RequestBody ConsumerRequest consumerRequest) {
        return consumerService.updateConsumer(id, consumerRequest);
    }

    @DeleteMapping(value="/consumer/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ConsumerResponse deleteConsumer(@PathVariable Long id) {
        return consumerService.deleteConsumer(id);
    }
}
