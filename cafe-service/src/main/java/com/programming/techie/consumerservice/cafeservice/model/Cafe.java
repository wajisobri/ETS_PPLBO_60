package com.programming.techie.consumerservice.cafeservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.time.LocalTime;
import java.util.List;

@Document(value = "cafe")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Cafe {
    @Id
    private String id;
    private String name;
    private String address;
    private String email;
    private LocalTime openTime;
    private LocalTime closeTime;
    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;
}
