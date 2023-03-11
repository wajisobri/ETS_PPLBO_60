package com.programming.techie.cafeservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Document(value = "ingredient")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Ingredient {
    @Id
    private String id;
    private String name;
    private Integer quantity;
    private String unitOfMeasurement;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id", nullable = false)
    private Cafe cafe;
}
