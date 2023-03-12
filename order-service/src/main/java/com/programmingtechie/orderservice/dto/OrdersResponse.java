package com.programmingtechie.orderservice.dto;

import com.programmingtechie.orderservice.model.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrdersResponse {
    private Integer code;
    private String message;
    private List<Order> data;
}
