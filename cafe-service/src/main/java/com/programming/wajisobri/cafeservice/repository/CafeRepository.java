package com.programming.wajisobri.cafeservice.repository;

import com.programming.wajisobri.cafeservice.model.Cafe;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CafeRepository extends MongoRepository<Cafe, String> {
}
