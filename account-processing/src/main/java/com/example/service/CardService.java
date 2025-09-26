package com.example.service;

import com.example.dto.CardRequestDto;
import com.example.entity.Account;
import com.example.entity.Card;
import com.example.enums.AccountStatus;
import com.example.repository.AccountRepository;
import com.example.repository.CardRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class CardService {

    private final CardRepo cardRepo;
    private final AccountRepository accountRepo;

    private final AtomicInteger counter = new AtomicInteger(1);


    public CardService(CardRepo cardRepo, AccountRepository accountRepo) {
        this.cardRepo = cardRepo;
        this.accountRepo = accountRepo;
    }


    public void createCard(CardRequestDto cardDto) {
        // Находим аккаунт по clientId
        Optional<Account> accountOpt = accountRepo.findByClientId(cardDto.getClientId());

        if (accountOpt.isEmpty()) {
            throw new RuntimeException("Account not found for clientId: " + cardDto.getClientId());
        }

        Account account = accountOpt.get();

        // Проверяем статус аккаунта
        if (!AccountStatus.ACTIVE.equals(account.getStatus())) {
            throw new RuntimeException("Account is not active");
        }

        // Генерация уникального cardId
        String cardId = generateUniqueCardId();

        // Создаём карту
        Card card = Card.builder()
                .cardId(cardId)
                .accountId(account.getId())  // <- используем id найденного аккаунта
                .paymentSystem(cardDto.getPaymentSystem().toString())
                .status("ACTIVE")
                .build();

        cardRepo.save(card);

        System.out.println("Card created: " + card);
    }

    // Генерация уникального cardId
    public String generateUniqueCardId() {
        String cardId;
        do {
            cardId = String.format("CARD_%06d", counter.getAndIncrement());
        } while (cardRepo.existsByCardId(cardId));
        return cardId;
    }
}

