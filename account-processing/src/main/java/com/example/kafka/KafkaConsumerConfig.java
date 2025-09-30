package com.example.kafka;

import com.example.dto.CardRequestDto;
import com.example.dto.ClientProductDto;
import com.example.dto.PaymentMessageDto;
import com.example.dto.TransactionDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;


    @Bean
    public ConsumerFactory<String, ClientProductDto> clientProductConsumerFactory() {
        JsonDeserializer<ClientProductDto> deserializer = new JsonDeserializer<>(ClientProductDto.class);
        deserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(
                baseProps(),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ClientProductDto> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ClientProductDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(clientProductConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, TransactionDto> transactionConsumerFactory() {
        JsonDeserializer<TransactionDto> deserializer = new JsonDeserializer<>(TransactionDto.class);
        deserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(
                baseProps(),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, TransactionDto> cardKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, TransactionDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(transactionConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, CardRequestDto> cardConsumerFactory() {
        JsonDeserializer<CardRequestDto> deserializer = new JsonDeserializer<>(CardRequestDto.class);
        deserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(
                baseProps(),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CardRequestDto> cardListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, CardRequestDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(cardConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, PaymentMessageDto> paymentConsumerFactory() {
        JsonDeserializer<PaymentMessageDto> deserializer = new JsonDeserializer<>(PaymentMessageDto.class);
        deserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(
                baseProps(),
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PaymentMessageDto> paymentKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PaymentMessageDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(paymentConsumerFactory());
        return factory;
    }

    private Map<String, Object> baseProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return props;
    }
}