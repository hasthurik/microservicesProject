package com.example.dto;

import com.example.enums.Status;

import java.time.LocalDate;

public class CleintProductSaveDto {

    private Long clientId;

    private Long productId;

    private LocalDate openDate;

    private LocalDate closeDate;

    private Status status;
}
