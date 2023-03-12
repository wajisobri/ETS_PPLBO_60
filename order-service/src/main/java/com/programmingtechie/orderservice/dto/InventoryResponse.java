package com.programmingtechie.orderservice.dto;

import com.programmingtechie.orderservice.model.Ingredient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponse {
    private Integer code;
    private String message;
    private Ingredient data;
}
