package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class PaymentRegistryDto {

    private Long productRegistryId;

    private LocalDate paymentDate;

    private BigDecimal amount;

    private BigDecimal interestRateAmount;

    private BigDecimal debtAmount;

    private Boolean expired;

    private LocalDate paymentExpirationDate;


}


