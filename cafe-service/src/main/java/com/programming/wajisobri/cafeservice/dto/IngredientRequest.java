package com.programming.wajisobri.cafeservice.dto;

import com.programming.wajisobri.cafeservice.model.Cafe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IngredientRequest {
    private String name;
    private Integer quantity;
    private String unitOfMeasurement;
    private Cafe cafe;
}
