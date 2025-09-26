package com.example.service;

import com.example.dto.CardRequestDto;
import com.example.kafka.KafkaEventProducer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    private final KafkaEventProducer producer;

    public CardService(KafkaEventProducer producer) {
        this.producer = producer;
    }

    public ResponseEntity<String> request(CardRequestDto card) {

        System.out.println("CardRequestDtoMS1: " + card);
        producer.sendCardRequest(card);

         return ResponseEntity.ok().body("Request has been sent");
    }
}
