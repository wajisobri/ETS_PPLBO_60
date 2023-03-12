package com.programming.techie.consumerservice.cafeservice.repository;

import com.programming.techie.consumerservice.cafeservice.model.Cafe;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CafeRepository extends MongoRepository<Cafe, String> {
}
