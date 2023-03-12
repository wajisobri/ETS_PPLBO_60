package com.programming.techie.consumerservice.repository;

import com.programming.techie.consumerservice.model.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
}
