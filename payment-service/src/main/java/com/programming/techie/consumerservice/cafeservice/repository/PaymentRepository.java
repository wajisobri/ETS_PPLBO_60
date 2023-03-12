package com.programming.techie.consumerservice.cafeservice.repository;

import com.programming.techie.consumerservice.cafeservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
