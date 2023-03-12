package com.programming.techie.consumerservice.cafeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientNoCafeRequest {
    private String id;
    private String name;
    private Integer quantity;
    private String unitOfMeasurement;
}
