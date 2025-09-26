package com.example.enums;

public enum TransactionStatus {
    ALLOWED("allowed"),
    PROCESSING("processing"),
    COMPLETE("complete"),
    BLOCKED("blocked"),
    CANCELLED("cancelled");

    TransactionStatus(String value) {
    }
}
