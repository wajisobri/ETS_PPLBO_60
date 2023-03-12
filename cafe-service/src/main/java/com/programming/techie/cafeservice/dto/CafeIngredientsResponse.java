package com.programming.techie.cafeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CafeIngredientsResponse {
    private Integer code;
    private String message;
    private List<IngredientNoCafeRequest> data;
}
