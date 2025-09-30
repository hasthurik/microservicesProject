package com.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ClientCreditProductDto {

    private String clientId;

    private Long accountId;

    private Long producerId;

    private Double interestRate;

    private BigDecimal creditAmount;

    private Integer mountNumber;
}
