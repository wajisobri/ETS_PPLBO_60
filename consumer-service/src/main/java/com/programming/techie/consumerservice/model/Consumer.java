package com.programming.techie.consumerservice.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "t_consumers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Consumer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "consumer_id")
    private Long id;
    private String username;
    private String password;
    private String name;
    private String phoneNumber;
}
