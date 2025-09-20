package com.example.model;

import com.example.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "client_products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private Long clientId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "open_date", nullable = false)
    private LocalDate openDate;

    @Column(name = "close_date")
    private LocalDate closeDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
}
