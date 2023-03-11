package com.programming.techie.cafeservice.dto;

import com.programming.techie.cafeservice.model.Cafe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CafeIngredientResponse {
    private Integer code;
    private String message;
    private CafeIngredientRequest data;
}
