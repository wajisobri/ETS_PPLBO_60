package com.programming.wajisobri.cafeservice.repository;

import com.programming.wajisobri.cafeservice.model.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface IngredientRepository extends MongoRepository<Ingredient, String> {
    Optional<Ingredient> findByCafeIdAndId(String cafeId, String ingredientId);

    void deleteByCafeIdAndId(String cafeId, String ingredientId);

    Optional<Ingredient> findByCafeIdAndName(String cafeId, String ingredientName);
}
