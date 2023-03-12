package com.programming.techie.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItems {
    private Long id;
    private String menuId;
    private String menuName;
    private BigDecimal menuPrice;
    private Integer quantity;
}
