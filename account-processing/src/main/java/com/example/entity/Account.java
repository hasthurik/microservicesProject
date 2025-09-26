package com.example.entity;

import com.example.enums.AccountStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "client_id", nullable = false)
    private String clientId;

    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "balance")
    private BigDecimal balance;

    @Column(name = "interest_rate")
    private Double interestRate;

    @Column(name = "is_recalc", nullable = false)
    private Boolean isRecalc;

    @Column(name = "card_exist", nullable = false)
    private Boolean cardExist;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus status;
}
