package com.programming.wajisobri.consumerservice.repository;

import com.programming.wajisobri.consumerservice.model.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepository extends JpaRepository<Consumer, Long> {
}
