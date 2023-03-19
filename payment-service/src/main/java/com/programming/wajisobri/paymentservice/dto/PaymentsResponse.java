package com.programming.wajisobri.paymentservice.dto;

import com.programming.wajisobri.paymentservice.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentsResponse {
    private Integer code;
    private String message;
    private List<Payment> data;
}