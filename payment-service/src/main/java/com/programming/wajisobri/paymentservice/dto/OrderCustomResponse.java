package com.programming.wajisobri.paymentservice.dto;

import com.programming.wajisobri.paymentservice.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCustomResponse {
    private Integer code;
    private String message;
    private Order data;
}
