package com.example.service;

import com.example.dto.CardRequestDto;
import com.example.dto.ClientProductDto;
import com.example.dto.TransactionDto;
import com.example.entity.Account;
import com.example.enums.AccountStatus;
import com.example.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }

    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    public Account findById(Long id) {
        return accountRepository.findById(id).get();
    }

    //DC, CC, NS, PENS
    public void createAccount(ClientProductDto productDto) {
        Account account = Account.builder()
                .interestRate(0.0)
                .balance(new BigDecimal(0.0))
                .clientId(productDto.getClientId())
                .productId(productDto.getProductId())
                .cardExist(Boolean.TRUE)
                .isRecalc(false)
                .status(AccountStatus.ACTIVE)
                .build();
        log.info("Account created: " + account);
        accountRepository.save(account);
    }

    public void processTransaction(TransactionDto transactionDto) {
        System.out.println("listening...");
    }
}
