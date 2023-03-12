package com.programmingtechie.orderservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consumer {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
}
