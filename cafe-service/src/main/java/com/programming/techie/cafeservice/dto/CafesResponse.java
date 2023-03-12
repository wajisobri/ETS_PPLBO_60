package com.programming.techie.cafeservice.dto;

import com.programming.techie.cafeservice.model.Cafe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CafesResponse {
    private Integer code;
    private String message;
    private List<Cafe> data;
}
