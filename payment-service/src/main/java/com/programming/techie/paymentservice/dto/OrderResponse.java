package com.programming.techie.paymentservice.dto;

import com.programming.techie.paymentservice.model.OrderLineItems;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    public enum OrderStatus {
        Created, Unpaid, Paid, Finished, Cancelled
    }

    private Long id;
    private String orderNumber;
    private String username;
    private String restaurantId;
    private LocalDateTime orderTime = LocalDateTime.now();
    private OrderStatus orderStatus = OrderStatus.Created;
    private BigDecimal totalPrice;
    private List<OrderLineItems> orderLineItemsList;
}
