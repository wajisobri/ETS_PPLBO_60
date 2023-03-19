package com.programming.wajisobri.paymentservice.repository;

import com.programming.wajisobri.paymentservice.model.PaymentEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentEventRepository extends MongoRepository<PaymentEvent, String> {
}
