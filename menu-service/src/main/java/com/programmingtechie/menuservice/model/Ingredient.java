package com.programmingtechie.menuservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {
    private String name;
    private Integer quantity;
    private String unitOfMeasurement;
}
