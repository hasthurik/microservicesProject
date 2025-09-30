package com.example.enums;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

public enum TransactionType {
    DEPOSIT("Deposit"),     // пополнение (зачисление денег на счёт)
    WITHDRAW("Withdraw"),    // списание (снятие/оплата)
    TRANSFER("Transfer"),    // перевод между счетами
    PAYMENT("Payment"),     // платёж по кредиту/ежемесячный платёж
    FEE("Fee"),         // комиссия банка
    INTEREST("Interest"),    // начисление процентов
    REFUND("Refund");      // возврат средств


    TransactionType(Object s) {}
}
