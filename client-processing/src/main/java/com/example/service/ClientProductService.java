package com.example.service;

import com.example.dto.ClientProductDto;
import com.example.dto.mapstruct.ClientProductMapper;
import com.example.entity.ClientProduct;
import com.example.kafka.KafkaEventProducer;
import com.example.repository.ClientProductRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ClientProductService {

    private final ClientProductRepo repo;
    private final KafkaEventProducer producer;
    private final ClientProductMapper mapper;

    public ClientProductService(ClientProductRepo repo, KafkaEventProducer producer, ClientProductMapper mapper) {
        this.repo = repo;
        this.producer = producer;
        this.mapper = mapper;
    }

    public ResponseEntity<Optional<ClientProduct>> getById(Long id) {
        if (repo.existsById(id)) {
            return new ResponseEntity<>(repo.findById(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<ClientProductDto> create(ClientProductDto clientProductDto) {

        ClientProduct savedClientProduct = mapper.toEntity(clientProductDto);

        String key = String.valueOf(clientProductDto.getProductKey());

        repo.save(savedClientProduct);

        switch (key) {
            case "IPO":
            case "PC":
            case "AC":
                producer.sendClientCreditProducts(clientProductDto);
                break;
            case "DC":
            case "CC":
            case "NS":
            case "PENS":
                producer.sendClientProduct(clientProductDto);
                break;
        }
        return new ResponseEntity<>(clientProductDto, HttpStatus.CREATED);
    }


    public ResponseEntity<ClientProduct> update(ClientProduct clientProduct) {
        return new ResponseEntity<>(repo.save(clientProduct), HttpStatus.OK);
    }

    public ResponseEntity<List<ClientProduct>> getAll() {
        return new ResponseEntity<>(repo.findAll(), HttpStatus.OK);
    }

    public ResponseEntity<String> deleteById(Long id) {

        if (repo.existsById(id)) {
            repo.deleteById(id);
            return new ResponseEntity<>("ClientProduct id = " + id + "delete!", HttpStatus.OK);
        }
        return new ResponseEntity<>("ClientProduct id = "+id + " not found", HttpStatus.NOT_FOUND);
    }
}
