package com.example.kafka;

import com.example.dto.CardRequestDto;
import com.example.dto.ClientProductDto;
import com.example.entity.ClientProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class KafkaEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${topics.client-products}")
    private String clientProductsTopic;

    @Value("${topics.client-credit-products}")
    private String clientCreditProductsTopic;

    @Value("${topics.client-cards}")
    private String clientCardsTopic;

    public void sendClientProduct(ClientProductDto product) {
        kafkaTemplate.send(clientProductsTopic, product);
    }

    public void sendClientCreditProducts(ClientProductDto product)
    {
        kafkaTemplate.send(clientCreditProductsTopic, product);
    }

    public void sendCardRequest(CardRequestDto card) {
        kafkaTemplate.send(clientCardsTopic, card);
    }
}
