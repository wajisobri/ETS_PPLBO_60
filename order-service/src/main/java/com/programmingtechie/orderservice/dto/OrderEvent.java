package com.programmingtechie.orderservice.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Component
public class OrderEvent {
    private String orderId;
    private BigDecimal amount;
}
