package com.programming.techie.consumerservice.dto;

import com.programming.techie.consumerservice.model.Consumer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerResponse {
    private Integer code;
    private String message;
    private Consumer data;
}
