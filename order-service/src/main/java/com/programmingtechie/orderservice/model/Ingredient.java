package com.programmingtechie.orderservice.model;

import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient {
    private String name;
    private Integer quantity;
    private String unitOfMeasurement;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe;

    public Ingredient(Ingredient ingredient) {
    }
}
