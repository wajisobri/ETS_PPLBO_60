package com.programming.techie.cafeservice.repository;

import com.programming.techie.cafeservice.model.Cafe;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CafeRepository extends MongoRepository<Cafe, String> {
}
