package com.example.service;

import com.example.dto.TransactionDto;
import com.example.entity.Account;
import com.example.entity.Payment;
import com.example.entity.Transaction;
import com.example.enums.AccountStatus;
import com.example.enums.TransactionStatus;
import com.example.enums.TransactionType;
import com.example.repository.AccountRepo;
import com.example.repository.PaymentRepo;
import com.example.repository.TransactionRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class TransactionService {

    private final AccountRepo accountRepo;
    private final TransactionRepo transactionRepo;
    private TransactionType transactionType;
    private final PaymentService paymentService;
    private final PaymentRepo paymentRepo;

    private static final int maxTransactions = 5;
    private static final Duration maxTime = Duration.ofMinutes(1);
    private final Map<Long, List<LocalDateTime>> cardTxLog = new HashMap<>();

    public TransactionService(AccountRepo accountRepository, TransactionRepo transactionRepo, PaymentService paymentService, PaymentRepo paymentRepo) {
        this.accountRepo = accountRepository;
        this.transactionRepo = transactionRepo;
        this.paymentService = paymentService;
        this.paymentRepo = paymentRepo;
    }

    @Transactional
    public void processTransaction(TransactionDto dto) {

        //поиск по id
        Account account = accountRepo.findByClientId(dto.getAccountId())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (account.getStatus() == AccountStatus.ARRESTED || account.getStatus() == AccountStatus.BLOCKED) {
            log.warn("Account status is not ACTIVE");
            return;
        }

        //кредитный счет
        if (account.getIsRecalc() == Boolean.TRUE) {
            paymentService.generateShedule(account);
        }

        //слишком много транзакций по карте
        cardTxLog.putIfAbsent(dto.getCardId(), new ArrayList<>());
        List<LocalDateTime> txTimes = cardTxLog.get(dto.getCardId());
        txTimes.add(dto.getTimestamp());
        txTimes.removeIf(t -> t.isBefore(LocalDateTime.now().minus(maxTime)));

        if (txTimes.size() > maxTransactions) {
            account.setStatus(AccountStatus.BLOCKED);
            accountRepo.save(account);
            log.warn("Account {} blocked due to too many tx", account.getId());
            return;
        }

        //DEPOSIT
        if (dto.getType() == TransactionType.DEPOSIT) {
            //если счет кредитный
            if (account.getIsRecalc() == Boolean.TRUE) {

                //ближайший платеж
                Optional<Payment> nextPaymentOpt = paymentRepo.findFirstByAccountIdAndExpiredFalseOrderByPaymentDateAsc(account.getId());
                if (nextPaymentOpt.isPresent()) {
                    Payment nextPayment = nextPaymentOpt.get();

                    // Проверяем: настал ли день платежа
                    if (LocalDate.now().isEqual(nextPayment.getPaymentDate().toLocalDate())) {

                        BigDecimal monthlyAmount = nextPayment.getAmount();

                        if (account.getBalance().compareTo(monthlyAmount) >= 0) {
                            // Баланс достаточный - списываем
                            account.setBalance(account.getBalance().subtract(monthlyAmount));
                            accountRepo.save(account);

                            nextPayment.setExpired(false); // платеж успешен
                            paymentRepo.save(nextPayment);

                            log.info("успешное списание");
                        } else {
                            // Недостаточно средств - отмечаем как просроченный
                            nextPayment.setExpired(true);
                            paymentRepo.save(nextPayment);

                            log.warn("Недостаточно средств");
                        }
                    }
                }
            } else {
                account.setBalance(account.getBalance().add(dto.getAmount()));
                transactionType = TransactionType.DEPOSIT;
                accountRepo.save(account);
            }
        }

        //WITHDRAW
        if (dto.getType() == TransactionType.WITHDRAW) {
            if (account.getBalance().compareTo(dto.getAmount()) < 0) {
                log.warn("Account balance is negative");
                return;
            } else {
                account.setBalance(account.getBalance().subtract(dto.getAmount()));
                transactionType = TransactionType.WITHDRAW;
                accountRepo.save(account);
            }
        }

        //сохраняем транзакцию
        Transaction transaction = Transaction.builder()
                .CardId(dto.getCardId())
                .type(transactionType)
                .accountId(dto.getAccountId())
                .CardId(dto.getCardId())
                .timestamp(dto.getTimestamp())
                .status(TransactionStatus.COMPLETE)
                .build();
        transactionRepo.save(transaction);
    }

}
