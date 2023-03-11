package com.programming.techie.cafeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CafeRequest {
    private String name;
    private String address;
    private String email;
    private LocalTime openTime;
    private LocalTime closeTime;
}
