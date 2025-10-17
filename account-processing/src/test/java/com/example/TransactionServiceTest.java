package com.example;

import com.example.dto.TransactionDto;
import com.example.entity.Account;
import com.example.entity.Payment;
import com.example.entity.Transaction;
import com.example.enums.AccountStatus;
import com.example.enums.TransactionType;
import com.example.repository.AccountRepo;
import com.example.repository.PaymentRepo;
import com.example.repository.TransactionRepo;
import com.example.service.PaymentService;
import com.example.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private AccountRepo accountRepo;
    @Mock
    private TransactionRepo transactionRepo;
    @Mock
    private PaymentService paymentService;
    @Mock
    private PaymentRepo paymentRepo;

    @InjectMocks
    private TransactionService service;

    private Account account;
    private TransactionDto dto;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setId(1L);
        account.setClientId(String.valueOf(1L));
        account.setBalance(BigDecimal.valueOf(1000));
        account.setStatus(AccountStatus.ACTIVE);
        account.setIsRecalc(false);

        dto = new TransactionDto();
        dto.setAccountId(String.valueOf(1L));
        dto.setCardId(100L);
        dto.setAmount(BigDecimal.valueOf(200));
        dto.setType(TransactionType.DEPOSIT);
        dto.setTimestamp(LocalDateTime.now());
    }


    @Test
    void shouldReturn_whenAccountBlocked() {
        account.setStatus(AccountStatus.BLOCKED);
        when(accountRepo.findByClientId(String.valueOf(1L))).thenReturn(Optional.of(account));

        service.processTransaction(dto);

        verify(accountRepo, never()).save(any());
        verify(transactionRepo, never()).save(any());
    }

    @Test
    void shouldCallGenerateSchedule_whenCreditAccount() {
        account.setIsRecalc(true);
        when(accountRepo.findByClientId(String.valueOf(1L))).thenReturn(Optional.of(account));

        service.processTransaction(dto);

        verify(paymentService).generateShedule(account);
    }

    @Test
    void shouldBlockAccount_whenTooManyTransactions() {
        when(accountRepo.findByClientId(String.valueOf(1L))).thenReturn(Optional.of(account));

        // имитируем 6 транзакций за минуту
        for (int i = 0; i < 6; i++) {
            dto.setTimestamp(LocalDateTime.now());
            service.processTransaction(dto);
        }

        verify(accountRepo, atLeastOnce()).save(account);
        assertThat(account.getStatus()).isEqualTo(AccountStatus.BLOCKED);
    }

    @Test
    void shouldIncreaseBalance_whenDepositOnDebitAccount() {
        when(accountRepo.findByClientId(String.valueOf(1L))).thenReturn(Optional.of(account));

        service.processTransaction(dto);

        verify(accountRepo).save(account);
        verify(transactionRepo).save(any(Transaction.class));
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(1200));
    }

    @Test
    void shouldWithdraw_whenEnoughBalance() {
        dto.setType(TransactionType.WITHDRAW);
        when(accountRepo.findByClientId(String.valueOf(1L))).thenReturn(Optional.of(account));

        service.processTransaction(dto);

        verify(accountRepo).save(account);
        verify(transactionRepo).save(any(Transaction.class));
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(800));
    }

    @Test
    void shouldNotWithdraw_whenInsufficientFunds() {
        dto.setType(TransactionType.WITHDRAW);
        dto.setAmount(BigDecimal.valueOf(2000));
        when(accountRepo.findByClientId(String.valueOf(1L))).thenReturn(Optional.of(account));

        service.processTransaction(dto);

        verify(accountRepo, never()).save(any());
        verify(transactionRepo, never()).save(any());
    }

    @Test
    void shouldMarkPaymentExpired_whenInsufficientBalance() {
        account.setIsRecalc(true);
        account.setBalance(BigDecimal.valueOf(50));

        Payment payment = Payment.builder()
                .paymentDate(LocalDateTime.now())
                .amount(BigDecimal.valueOf(100))
                .expired(false)
                .build();

        when(accountRepo.findByClientId(String.valueOf(1L))).thenReturn(Optional.of(account));
        when(paymentRepo.findFirstByAccountIdAndExpiredFalseOrderByPaymentDateAsc(1L))
                .thenReturn(Optional.of(payment));

        service.processTransaction(dto);

        verify(paymentRepo).save(payment);
        assertThat(payment.getExpired()).isTrue();
    }

    @Test
    void shouldPayMonthlyPayment_whenEnoughBalance() {
        account.setIsRecalc(true);
        account.setBalance(BigDecimal.valueOf(500));

        Payment payment = Payment.builder()
                .paymentDate(LocalDateTime.now())
                .amount(BigDecimal.valueOf(100))
                .expired(false)
                .build();

        when(accountRepo.findByClientId(String.valueOf(1L))).thenReturn(Optional.of(account));
        when(paymentRepo.findFirstByAccountIdAndExpiredFalseOrderByPaymentDateAsc(1L))
                .thenReturn(Optional.of(payment));

        service.processTransaction(dto);

        verify(paymentRepo).save(payment);
        verify(accountRepo).save(account);
        assertThat(payment.getExpired()).isFalse();
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(400));
    }
}

