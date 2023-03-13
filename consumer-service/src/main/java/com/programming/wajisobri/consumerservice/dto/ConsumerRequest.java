package com.programming.wajisobri.consumerservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsumerRequest {
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
}
