package com.example.service;

import com.example.dto.PaymentMessageDto;
import com.example.entity.Account;
import com.example.entity.Payment;
import com.example.repository.AccountRepo;
import com.example.repository.PaymentRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class PaymentService {

    private final PaymentRepo paymentRepo;
    private final AccountRepo accountRepo;

    @Value("${credit.creditAmount}")
    private BigDecimal creditAmount;

    @Value("${credit.months}")
    private int months;

    public PaymentService(PaymentRepo paymentRepo, AccountRepo accountRepo) {
        this.paymentRepo = paymentRepo;
        this.accountRepo = accountRepo;
    }

    public void generateShedule(Account account) {

        BigDecimal monthlyRate = BigDecimal.valueOf(account.getInterestRate())
                .divide(BigDecimal.valueOf(12 * 100), 10, RoundingMode.HALF_UP);

        // ежемесячный платеж
        BigDecimal numerator = monthlyRate.multiply((BigDecimal.ONE.add(monthlyRate)).pow(months));
        BigDecimal denominator = (BigDecimal.ONE.add(monthlyRate)).pow(months).subtract(BigDecimal.ONE);
        BigDecimal annuityPayment = creditAmount.multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP);

        // создаём список платежей
        for (int month = 1; month <= months; month++) {
            Payment payment = Payment.builder()
                    .accountId(account.getId())
                    .paymentDate(LocalDateTime.now().plusMonths(month))
                    .amount(annuityPayment)
                    .isCredit(true)
                    .type("MONTHLY")
                    .expired(false)
                    .build();
            paymentRepo.save(payment);
        }
    }


    @Transactional
    public void processPayment(PaymentMessageDto dto) {
        Account account = accountRepo.findById(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getIsRecalc() != Boolean.TRUE) {
            log.warn("Account is not credit, id = {}", account.getId() );
            return;
        }
        //общая сумма задолжности
        BigDecimal totalDebt = paymentRepo.findByAccountIdAndPayedAtIsNull(account.getId())
                .stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //выполняем платеж
        if (dto.getAmount().compareTo(totalDebt) == 0) {
            account.setBalance(account.getBalance().subtract(dto.getAmount()));
            accountRepo.save(account);

            Payment payment = Payment.builder()
                    .accountId(account.getId())
                    .amount(dto.getAmount())
                    .paymentDate(dto.getDate())
                    .isCredit(true)
                    .expired(false)
                    .type("FULL_PAYMENT")
                    .build();
            payment.setPayedAt(LocalDateTime.now());
            paymentRepo.save(payment);

            List<Payment> payments = paymentRepo.findByAccountIdAndPayedAtIsNull(account.getId());
            for (Payment p : payments) {
                p.setPayedAt(LocalDateTime.now());
                p.setExpired(false);
            }
            paymentRepo.saveAll(payments);
        }
    }
}
