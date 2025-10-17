package com.example;

import com.example.dto.PaymentMessageDto;
import com.example.entity.Account;
import com.example.entity.Payment;
import com.example.repository.AccountRepo;
import com.example.repository.PaymentRepo;
import com.example.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepo paymentRepo;

    @Mock
    private AccountRepo accountRepo;

    @InjectMocks
    private PaymentService paymentService;

    private Account account;

    @BeforeEach
    void setUp() throws Exception {
        ReflectionTestUtils.setField(paymentService, "creditAmount", new BigDecimal("120000"));
        ReflectionTestUtils.setField(paymentService, "months", 12);

        account = new Account();
        account.setId(1L);
        account.setInterestRate(12.0);
        account.setIsRecalc(true);
        account.setBalance(new BigDecimal("200000"));
    }

    @Test
    void generateShedule_shouldSaveMonthlyPayments() {
        paymentService.generateShedule(account);

        verify(paymentRepo, times(12)).save(any(Payment.class));
    }

    @Test
    void processPayment_shouldHandleFullPaymentCorrectly() {
        PaymentMessageDto dto = new PaymentMessageDto();
        dto.setAccountId(1L);
        dto.setAmount(new BigDecimal("120000"));
        dto.setDate(LocalDateTime.now());

        when(accountRepo.findById(1L)).thenReturn(Optional.of(account));

        List<Payment> unpaidPayments = List.of(
                Payment.builder().amount(new BigDecimal("60000")).build(),
                Payment.builder().amount(new BigDecimal("60000")).build()
        );
        when(paymentRepo.findByAccountIdAndPayedAtIsNull(1L)).thenReturn(unpaidPayments);

        paymentService.processPayment(dto);

        verify(accountRepo).save(any(Account.class));          // баланс обновился
        verify(paymentRepo).save(any(Payment.class));          // новый платёж FULL_PAYMENT
        verify(paymentRepo).saveAll(anyList());                // обновление всех оставшихся
    }

    @Test
    void processPayment_shouldDoNothing_whenAccountNotRecalc() {
        account.setIsRecalc(false);
        when(accountRepo.findById(1L)).thenReturn(Optional.of(account));

        PaymentMessageDto dto = new PaymentMessageDto();
        dto.setAccountId(1L);
        dto.setAmount(new BigDecimal("1000"));
        dto.setDate(LocalDateTime.now());

        paymentService.processPayment(dto);

        verify(paymentRepo, never()).save(any());
        verify(accountRepo, never()).save(any());
    }

    @Test
    void processPayment_shouldThrow_whenAccountNotFound() {
        when(accountRepo.findById(999L)).thenReturn(Optional.empty());

        PaymentMessageDto dto = new PaymentMessageDto();
        dto.setAccountId(999L);
        dto.setAmount(new BigDecimal("1000"));
        dto.setDate(LocalDateTime.now());

        assertThatThrownBy(() -> paymentService.processPayment(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Account not found");
    }
}
