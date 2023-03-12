package com.programmingtechie.orderservice.model;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.time.LocalTime;
import java.util.List;

public class Cafe {
    private String id;
    private String name;
    private String address;
    private String email;
    private LocalTime openTime;
    private LocalTime closeTime;
    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;
}
