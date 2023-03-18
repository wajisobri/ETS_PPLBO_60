package com.programming.wajisobri.paymentservice.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    public enum OrderStatus {
        Created, Unpaid, Paid, Preparing, Delivering, Finished, Cancelled
    }

    @Id
    private Long id;
    private String orderNumber;
    private String username;
    private String restaurantId;
    private LocalDateTime orderTime = LocalDateTime.now();
    private OrderStatus orderStatus = OrderStatus.Created;
    private BigDecimal totalPrice;
    private List<OrderLineItems> orderLineItemsList;
}

