package com.programming.wajisobri.menuservice.dto;

import com.programming.wajisobri.menuservice.model.Menu;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponse {
    private Integer code;
    private String message;
    private Menu data;
}
