package com.programming.techie.consumerservice.cafeservice.service;

import com.programming.techie.consumerservice.cafeservice.model.Cafe;
import com.programming.techie.consumerservice.cafeservice.model.Ingredient;
import com.programming.techie.consumerservice.cafeservice.repository.CafeRepository;
import com.programming.techie.consumerservice.cafeservice.repository.IngredientRepository;
import com.programming.techie.consumerservice.cafeservice.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CafeService {
    private final CafeRepository cafeRepository;
    private final IngredientRepository ingredientRepository;
    private final WebClient.Builder webClientBuilder;

    public CafeResponse createCafe(CafeRequest cafeRequest) {
        try {
            Cafe cafe = Cafe.builder()
                    .name(cafeRequest.getName())
                    .address(cafeRequest.getAddress())
                    .email(cafeRequest.getEmail())
                    .openTime(cafeRequest.getOpenTime())
                    .closeTime(cafeRequest.getCloseTime())
                    .build();

            cafe = cafeRepository.save(cafe);
            return CafeResponse.builder()
                    .code(200)
                    .message("Cafe created")
                    .data(cafe)
                    .build();
        } catch (DataAccessException e) {
            return CafeResponse.builder()
                    .code(500)
                    .message("An error occurred while accessing the database")
                    .build();
        } catch (Exception e) {
            return CafeResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
    }

    public CafesResponse getAllCafes() {
        try {
            List<Cafe> cafes = cafeRepository.findAll();

            if (cafes.isEmpty()) {
                return CafesResponse.builder()
                        .code(404)
                        .message("No cafe found")
                        .build();
            }

            cafes = cafes.stream().map(this::mapToCafeResponse).toList();
            return CafesResponse.builder()
                    .code(200)
                    .message("List of cafe retrieved")
                    .data(cafes)
                    .build();
        } catch (DataAccessException e) {
            return CafesResponse.builder()
                    .code(500)
                    .message("An error occurred while accessing the database")
                    .build();
        } catch (Exception e) {
            return CafesResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
    }

    private Cafe mapToCafeResponse(Cafe cafe) {
        return Cafe.builder()
                .id(cafe.getId())
                .name(cafe.getName())
                .address(cafe.getAddress())
                .email(cafe.getEmail())
                .openTime(cafe.getOpenTime())
                .closeTime(cafe.getCloseTime())
                .build();
    }

    public CafeResponse updateCafe(@PathVariable String id, @RequestBody CafeRequest cafeRequest) {
        try {
            Optional<Cafe> cafeRepo = cafeRepository.findById(id);
            if(cafeRepo.isEmpty()) {
                return CafeResponse.builder()
                        .code(404)
                        .message("Cafe with id " + id + " not found")
                        .build();
            }

            Cafe updatedCafe = cafeRepo.get();
            if (cafeRequest.getName() != null) {
                updatedCafe.setName(cafeRequest.getName());
            }
            if (cafeRequest.getAddress() != null) {
                updatedCafe.setAddress(cafeRequest.getAddress());
            }
            if (cafeRequest.getEmail() != null) {
                updatedCafe.setEmail(cafeRequest.getEmail());
            }
            if (cafeRequest.getOpenTime() != null) {
                updatedCafe.setOpenTime(cafeRequest.getOpenTime());
            }
            if (cafeRequest.getCloseTime() != null) {
                updatedCafe.setCloseTime(cafeRequest.getCloseTime());
            }

            Cafe savedCafe = cafeRepository.save(updatedCafe);
            if(savedCafe != null) {
                return CafeResponse.builder()
                        .code(200)
                        .message("Cafe updated")
                        .data(savedCafe)
                        .build();
            } else {
                return CafeResponse.builder()
                        .code(500)
                        .message("Failed to update cafe")
                        .build();
            }
        } catch (DataAccessException e) {
            return CafeResponse.builder()
                    .code(500)
                    .message("An error occurred while accessing the database")
                    .build();
        } catch (Exception e) {
            return CafeResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
    }

    public CafeResponse getCafe(String id) {
        try {
            Optional<Cafe> cafe = cafeRepository.findById(id);
            return CafeResponse.builder()
                    .code(200)
                    .message("Cafe with id " + id + " retrieved")
                    .data(cafe.get())
                    .build();
        } catch (NoSuchElementException e) {
            return CafeResponse.builder()
                    .code(404)
                    .message("Cafe with id " + id + " not found")
                    .build();
        } catch (Exception e) {
            return CafeResponse.builder()
                    .code(500)
                    .message(e.getMessage())
                    .build();
        }
    }

    public CafeResponse deleteCafe(String id) {
        try {
            Optional<Cafe> cafe = cafeRepository.findById(id);
            if (!cafe.isPresent()) {
                return CafeResponse.builder()
                        .code(404)
                        .message("Cafe with id " + id + " not found")
                        .build();
            }
            cafeRepository.deleteById(id);
            return CafeResponse.builder()
                    .code(200)
                    .message("Cafe with id " + id + " deleted")
                    .data(cafe.get())
                    .build();
        } catch (DataAccessException e) {
            return CafeResponse.builder()
                    .code(500)
                    .message("An error occurred while accessing the database")
                    .build();
        } catch (Exception e) {
            return CafeResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
    }

    public CafeIngredientsResponse getAllIngredient(String cafeId) {
        try {
            List<Ingredient> ingredients = ingredientRepository.findAll();

            List<IngredientNoCafeRequest> ingredientNoCafeRequest = ingredients.stream().map(this::mapToIngredientResponse).toList();
            return CafeIngredientsResponse.builder()
                    .code(200)
                    .message("Ingredients retrieved")
                    .data(ingredientNoCafeRequest)
                    .build();
        } catch (DataAccessException e) {
            return CafeIngredientsResponse.builder()
                    .code(500)
                    .message("An error occurred while accessing the database")
                    .build();
        } catch (Exception e) {
            return CafeIngredientsResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
    }

    private IngredientNoCafeRequest mapToIngredientResponse(Ingredient ingredient) {
        return IngredientNoCafeRequest.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .quantity(ingredient.getQuantity())
                .unitOfMeasurement(ingredient.getUnitOfMeasurement())
                .build();
    }

    public IngredientResponse getIngredient(String cafeId, String ingredientId) {
        try {
            Optional<Ingredient> ingredient = ingredientRepository.findByCafeIdAndId(cafeId, ingredientId);
            if (!ingredient.isPresent()) {
                return IngredientResponse.builder()
                        .code(404)
                        .message("Ingredient with id " + ingredientId + " in cafe with id " + cafeId + " not found")
                        .build();
            }
            Ingredient retrievedIngredient = Ingredient.builder()
                    .name(ingredient.get().getName())
                    .quantity(ingredient.get().getQuantity())
                    .unitOfMeasurement(ingredient.get().getUnitOfMeasurement())
                    .cafe(ingredient.get().getCafe())
                    .build();
            return IngredientResponse.builder()
                    .code(200)
                    .message("Ingredient with id " + ingredientId + " in cafe with id " + cafeId + " found")
                    .data(retrievedIngredient)
                    .build();
        } catch (NoSuchElementException e) {
            return IngredientResponse.builder()
                    .code(404)
                    .message("Ingredient with id " + ingredientId + " in cafe with id " + cafeId + " not found")
                    .build();
        } catch (Exception e) {
            return IngredientResponse.builder()
                    .code(500)
                    .message(e.getMessage())
                    .build();
        }
    }

    public IngredientResponse addIngredient(String cafeId, IngredientRequest ingredientRequest) {
        Optional<Cafe> optionalCafe = cafeRepository.findById(cafeId);
        Cafe cafe = null;
        try {
            cafe = optionalCafe.get();
            ingredientRequest.setCafe(cafe);
            try {
                Ingredient ingredient = Ingredient.builder()
                        .name(ingredientRequest.getName())
                        .quantity(ingredientRequest.getQuantity())
                        .unitOfMeasurement(ingredientRequest.getUnitOfMeasurement())
                        .cafe(ingredientRequest.getCafe())
                        .build();

                ingredient = ingredientRepository.save(ingredient);
                return IngredientResponse.builder()
                        .code(200)
                        .message("Ingredient added to cafe with " + cafeId)
                        .data(ingredient)
                        .build();
            } catch (DataAccessException e) {
                return IngredientResponse.builder()
                        .code(500)
                        .message("An error occurred while accessing the database")
                        .build();
            } catch (Exception e) {
                return IngredientResponse.builder()
                        .code(500)
                        .message("An error occurred while processing the request")
                        .build();
            }
        } catch (NoSuchElementException e) {
            return IngredientResponse.builder()
                    .code(404)
                    .message("Cafe with id " + cafeId + " not found")
                    .build();
        } catch (Exception e) {
            return IngredientResponse.builder()
                    .code(500)
                    .message(e.getMessage())
                    .build();
        }
    }

    public IngredientResponse deleteIngredient(String cafeId, String ingredientId) {
        try {
            Optional<Ingredient> ingredient = ingredientRepository.findByCafeIdAndId(cafeId, ingredientId);
            if (!ingredient.isPresent()) {
                return IngredientResponse.builder()
                        .code(404)
                        .message("Ingredient with id " + ingredientId + " in cafe with id " + cafeId + " not found")
                        .build();
            }
            ingredientRepository.deleteByCafeIdAndId(cafeId, ingredientId);
            return IngredientResponse.builder()
                    .code(200)
                    .message("Ingredient with id " + ingredientId + " in cafe with id " + cafeId + " deleted")
                    .data(ingredient.get())
                    .build();
        } catch (DataAccessException e) {
            return IngredientResponse.builder()
                    .code(500)
                    .message("An error occurred while accessing the database")
                    .build();
        } catch (Exception e) {
            return IngredientResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
    }

    public IngredientResponse updateIngredient(String cafeId, String ingredientId, IngredientRequest ingredientRequest) {
        Optional<Ingredient> ingredientRepo = ingredientRepository.findByCafeIdAndId(cafeId, ingredientId);
        if (!ingredientRepo.isPresent()) {
            return IngredientResponse.builder()
                    .code(404)
                    .message("Ingredient with id " + ingredientId + " in cafe with id " + cafeId + " not found")
                    .build();
        }

        Ingredient updatedIngredient = ingredientRepo.get();
        if (ingredientRequest.getName() != null) {
            updatedIngredient.setName(ingredientRequest.getName());
        }
        if (ingredientRequest.getQuantity() != null) {
            updatedIngredient.setQuantity(ingredientRequest.getQuantity());
        }
        if (ingredientRequest.getUnitOfMeasurement() != null) {
            updatedIngredient.setUnitOfMeasurement(ingredientRequest.getUnitOfMeasurement());
        }

        try {
            updatedIngredient.setId(ingredientRepo.get().getId());
            updatedIngredient.setCafe(ingredientRepo.get().getCafe());
            updatedIngredient = ingredientRepository.save(updatedIngredient);

            return IngredientResponse.builder()
                    .code(200)
                    .message("Ingredient updated with id " + ingredientId + " to cafe with " + cafeId)
                    .data(updatedIngredient)
                    .build();
        } catch (Exception e) {
            return IngredientResponse.builder()
                    .code(500)
                    .message("An error occurred while processing the request")
                    .build();
        }
    }
}
