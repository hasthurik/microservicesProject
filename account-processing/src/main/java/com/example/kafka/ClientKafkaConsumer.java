package com.example.kafka;


import com.example.dto.CardRequestDto;
import com.example.dto.ClientProductDto;
import com.example.dto.TransactionDto;
import com.example.repository.AccountRepository;
import com.example.service.AccountService;
import com.example.service.CardService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class ClientKafkaConsumer {

    private final AccountService accountService;
    private final AccountRepository accountRepository;
    private final CardService cardService;

    public ClientKafkaConsumer(AccountService accountService, AccountRepository accountRepository, CardService cardService) {
        this.accountService = accountService;
        this.accountRepository = accountRepository;
        this.cardService = cardService;
    }

    // Слушаем топик client_products
    @KafkaListener(
            topics = "client_products",
            groupId = "client-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenClientProducts(ClientProductDto clientProductDto) {
        // Вызываем сервис для создания продукта
        accountService.createAccount(clientProductDto);
        System.out.println("Product created: " + clientProductDto);
    }



    @KafkaListener(topics = "client_cards",
            groupId = "client-service",
            containerFactory = "cardKafkaListenerContainerFactory")
    public void listenClientTransactions(TransactionDto transactionDto) {
        accountService.processTransaction(transactionDto);
    }

    @KafkaListener(topics = "client_cards",
            groupId = "client-service",
            containerFactory = "cardKafkaListenerContainerFactory")
    public void listenClientCards(CardRequestDto cardDto) {
        try {
            cardService.createCard(cardDto);
        } catch (Exception e) {
            System.err.println("Failed to create card: " + e.getMessage());
        }
    }

}