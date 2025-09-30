package com.example.enums;

public enum AccountStatus {
    BLOCKED("Blocked"),
    OPENED("Opened"),
    ACTIVE("Active"),
    CLOSED("Closed"),
    FROZEN("Frozen"),
    ARRESTED("Arrested");

    AccountStatus(String value) {
    }
}
