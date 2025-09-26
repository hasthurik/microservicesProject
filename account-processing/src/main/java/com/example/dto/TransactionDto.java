package com.example.dto;

import com.example.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {

    private String accountId;

    private Long CardId;

    private String type;

    private TransactionStatus status;

    private LocalDateTime timestamp;
}