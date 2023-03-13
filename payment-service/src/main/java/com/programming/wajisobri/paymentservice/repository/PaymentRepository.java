package com.programming.wajisobri.paymentservice.repository;

import com.programming.wajisobri.paymentservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByOrderNumber(String orderNumber);
}
