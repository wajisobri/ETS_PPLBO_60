package com.programmingtechie.productservice.dto;

import com.programmingtechie.productservice.model.Menu;
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
public class MenuResponse {
    private Integer code;
    private String message;
    private Menu data;
}
