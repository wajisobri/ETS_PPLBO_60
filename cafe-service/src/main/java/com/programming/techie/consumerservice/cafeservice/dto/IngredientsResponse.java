package com.programming.techie.consumerservice.cafeservice.dto;

import com.programming.techie.consumerservice.cafeservice.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientsResponse {
    private Integer code;
    private String message;
    private List<Ingredient> data;
}
