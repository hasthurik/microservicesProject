package com.example.dto;

import com.example.enums.AccountStatus;
import com.example.enums.ProductKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientProductDto {

    private ProductKey productKey;

    private String clientId;

    private String productId;

    private LocalDate openDate;

    private AccountStatus status;
}

