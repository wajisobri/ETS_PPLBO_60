package com.programmingtechie.orderservice.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "t_orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    public enum OrderStatus {
        Created, Unpaid, Paid, Finished, Cancelled
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    private String orderNumber;
    private String username;
    private String restaurantId;
    private LocalDateTime orderTime = LocalDateTime.now();
    private OrderStatus orderStatus = OrderStatus.Created;
    private double totalPrice;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private List<OrderLineItems> orderLineItemsList;
}
