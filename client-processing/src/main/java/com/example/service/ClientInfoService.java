package com.example.service;

import com.example.dto.ClientInfoDto;
import com.example.entity.Client;
import com.example.repository.ClientRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClientInfoService {

    private final ClientRepo clientRepo;

    public ClientInfoService(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    public ClientInfoDto getData(String id) {

        Client client = clientRepo.findClientsByClientId(id).orElseThrow(() -> new EntityNotFoundException("Client with id " + id + " not found"));
        ClientInfoDto clientInfoDto = ClientInfoDto.builder()
                .documentNumber(client.getDocumentId())
                .firstName(client.getFirstName())
                .middleName(client.getMiddleName())
                .lastName(client.getLastName())
                .build();

        return clientInfoDto;
    }
}
