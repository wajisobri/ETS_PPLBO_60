package com.programming.techie.consumerservice.cafeservice.repository;

import com.programming.techie.consumerservice.cafeservice.model.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IngredientRepository extends MongoRepository<Ingredient, String> {
    Optional<Ingredient> findByCafeIdAndId(String cafeId, String ingredientId);

    void deleteByCafeIdAndId(String cafeId, String ingredientId);
}
