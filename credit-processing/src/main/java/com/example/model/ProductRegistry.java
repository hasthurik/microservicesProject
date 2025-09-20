package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_registry")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "account_id")
    private Long accountId;

    @Column(name = "producer_id")
    private Long producerId;

    @Column(name = "interest_rate")
    private Double interestRate;

    @Column(name = "open_date")
    private LocalDate openDate;

    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

}
