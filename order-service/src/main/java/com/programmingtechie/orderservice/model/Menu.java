package com.programmingtechie.orderservice.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Menu {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
