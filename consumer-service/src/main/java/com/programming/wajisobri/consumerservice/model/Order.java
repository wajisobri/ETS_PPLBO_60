package com.programming.wajisobri.consumerservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "t_consumer_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    public enum OrderStatus {
        Created, Unpaid, Paid, Finished, Cancelled
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;
    private float totalAmount;
    private OrderStatus orderStatus = OrderStatus.Created;
}
