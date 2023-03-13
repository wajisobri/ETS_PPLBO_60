package com.programming.wajisobri.paymentservice.dto;

import com.programming.wajisobri.paymentservice.model.Payment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private String orderNumber;
    private Payment.PaymentMethod paymentMethod;
}
