package com.programming.wajisobri.menuservice.dto;

import com.programming.wajisobri.menuservice.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private List<Ingredient> requiredIngredient;
}
