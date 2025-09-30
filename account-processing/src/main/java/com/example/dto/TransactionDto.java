package com.example.dto;

import com.example.enums.TransactionStatus;
import com.example.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    private UUID transactionId;

    private BigDecimal amount;

    private String accountId;

    private Long cardId;

    private TransactionType type;

    private TransactionStatus status;

    private LocalDateTime timestamp;
}