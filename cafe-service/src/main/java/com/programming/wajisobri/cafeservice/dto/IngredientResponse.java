package com.programming.wajisobri.cafeservice.dto;

import com.programming.wajisobri.cafeservice.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientResponse {
    private Integer code;
    private String message;
    private Ingredient data;
}
