package com.programming.wajisobri.cafeservice.controller;

import com.programming.wajisobri.cafeservice.dto.*;
import com.programming.wajisobri.cafeservice.service.CafeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CafeController {
    private final CafeService cafeService;

    @GetMapping(value="/cafes")
    @ResponseStatus(HttpStatus.OK)
    public CafesResponse getAllCafes() {
        return cafeService.getAllCafes();
    }

    @GetMapping(value="/cafe/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CafeResponse getCafe(@PathVariable String id) {
        return cafeService.getCafe(id);
    }

    @PostMapping(value="/cafe")
    @ResponseStatus(HttpStatus.CREATED)
    public CafeResponse createCafe(@RequestBody CafeRequest cafeRequest) {
        return cafeService.createCafe(cafeRequest);
    }

    @PutMapping(value="/cafe/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CafeResponse updateCafe(@PathVariable String id, @RequestBody CafeRequest cafeRequest) {
        return cafeService.updateCafe(id, cafeRequest);
    }

    @DeleteMapping(value="/cafe/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CafeResponse deleteCafe(@PathVariable String id) {
        return cafeService.deleteCafe(id);
    }

    @GetMapping(value="/cafe/ingredients/{cafeId}")
    @ResponseStatus(HttpStatus.OK)
    public CafeIngredientsResponse getAllIngredients(@PathVariable String cafeId) {
        return cafeService.getAllIngredient(cafeId);
    }

    @GetMapping(value="/cafe/ingredient/{cafeId}")
    @ResponseStatus(HttpStatus.OK)
    public IngredientResponse getIngredient(@PathVariable String cafeId, @RequestParam String ingredientName) {
        return cafeService.getIngredient(cafeId, ingredientName);
    }
    @PostMapping(value="/cafe/ingredient/{cafeId}")
    @ResponseStatus(HttpStatus.CREATED)
    public IngredientResponse createIngredient(@PathVariable String cafeId, @RequestBody IngredientRequest ingredientRequest) {
        return cafeService.addIngredient(cafeId, ingredientRequest);
    }

    @PutMapping(value="/cafe/ingredient/{cafeId}/{ingredientId}")
    @ResponseStatus(HttpStatus.OK)
    public IngredientResponse updateIngredient(@PathVariable String cafeId, @PathVariable String ingredientId, @RequestBody IngredientRequest ingredientRequest) {
        return cafeService.updateIngredient(cafeId, ingredientId, ingredientRequest);
    }

    @DeleteMapping(value="/cafe/ingredient/{cafeId}/{ingredientId}")
    @ResponseStatus(HttpStatus.OK)
    public IngredientResponse deleteIngredient(@PathVariable String cafeId, @PathVariable String ingredientId) {
        return cafeService.deleteIngredient(cafeId, ingredientId);
    }
}
