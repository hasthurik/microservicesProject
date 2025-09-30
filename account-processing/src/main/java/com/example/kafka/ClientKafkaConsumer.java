package com.example.kafka;


import com.example.dto.CardRequestDto;
import com.example.dto.ClientProductDto;
import com.example.dto.PaymentMessageDto;
import com.example.dto.TransactionDto;
import com.example.service.AccountService;
import com.example.service.CardService;
import com.example.service.PaymentService;
import com.example.service.TransactionService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ClientKafkaConsumer {

    private final AccountService accountService;
    private final TransactionService transactionService;
    private final CardService cardService;
    private final PaymentService paymentService;

    public ClientKafkaConsumer(AccountService accountService,
                               TransactionService transactionService,
                               CardService cardService,
                               PaymentService paymentService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
        this.cardService = cardService;
        this.paymentService = paymentService;
    }

    @KafkaListener(
            topics = "client_products",
            groupId = "client-service",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void listenClientProducts(ClientProductDto dto) {
        accountService.createAccount(dto);
        System.out.println("Product created: " + dto);
    }

    @KafkaListener(
            topics = "client_transactions",
            groupId = "client-service",
            containerFactory = "cardKafkaListenerContainerFactory"
    )
    public void listenClientTransactions(TransactionDto dto) {
        transactionService.processTransaction(dto);
        System.out.println("Transaction processed: " + dto);
    }

    @KafkaListener(
            topics = "client_cards",
            groupId = "client-service",
            containerFactory = "cardListenerContainerFactory"
    )
    public void listenClientCards(CardRequestDto dto) {
        try {
            cardService.createCard(dto);
            System.out.println("Card created: " + dto);
        } catch (Exception e) {
            System.err.println(" Failed to create card: " + e.getMessage());
        }
    }

    @KafkaListener(
            topics = "client_payments",
            groupId = "client-service",
            containerFactory = "paymentKafkaListenerContainerFactory"
    )
    public void listenClientPayments(PaymentMessageDto dto) {
        paymentService.processPayment(dto);
        System.out.println("Payment processed: " + dto);
    }
}
