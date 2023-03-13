package com.programming.wajisobri.paymentservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "t_payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    public enum PaymentStatus {
        Cancelled, Unpaid, Paid
    }

    public enum PaymentMethod {
        Transfer, Cash
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;
    private String paymentNumber;
    private String orderNumber;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentTime;
}
