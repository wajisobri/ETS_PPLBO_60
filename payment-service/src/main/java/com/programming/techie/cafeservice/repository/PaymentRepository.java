package com.programming.techie.cafeservice.repository;

import com.programming.techie.cafeservice.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
