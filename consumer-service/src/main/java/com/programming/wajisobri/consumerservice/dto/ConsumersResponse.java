package com.programming.wajisobri.consumerservice.dto;

import com.programming.wajisobri.consumerservice.model.Consumer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumersResponse {
    private Integer code;
    private String message;
    private List<Consumer> data;
}
