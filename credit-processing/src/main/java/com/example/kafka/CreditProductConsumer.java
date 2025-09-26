package com.example.kafka;

import com.example.dto.ClientCreditProductDto;
import com.example.service.CreditProductService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class CreditProductConsumer {

    private final CreditProductService creditProductService;

    public CreditProductConsumer(CreditProductService creditProductService) {
        this.creditProductService = creditProductService;
    }

    @KafkaListener(
            topics = "client_credit_products",
            groupId = "credit-service",
            containerFactory = "creditKafkaListenerContainerFactory"
    )
    public void listen(ClientCreditProductDto dto) {
        try {
            creditProductService.processCreditRequest(dto);
        } catch (Exception e) {
            System.err.println("Failed to process credit request: " + e.getMessage());
        }
    }
}
