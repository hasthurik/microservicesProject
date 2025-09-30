package com.example.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "payment_registry")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_registry_id", nullable = false)
    private Long productRegistryId;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "interest_rate_amount", nullable = false)
    private BigDecimal interestRateAmount;

    @Column(name = "debt_amount", nullable = false)
    private BigDecimal debtAmount;

    @Column(nullable = false)
    private Boolean expired;

    @Column(name = "payment_expiration_date", nullable = false)
    private LocalDate paymentExpirationDate;

}
