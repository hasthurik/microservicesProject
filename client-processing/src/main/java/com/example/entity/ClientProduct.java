package com.example.entity;

import com.example.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "client_products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ClientProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "open_date", nullable = false)
    private LocalDate openDate;

    @Column(name = "close_date")
    private LocalDate closeDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

}
