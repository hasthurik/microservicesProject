package com.example.service;

import com.example.dto.ClientRegistrationDto;
import com.example.entity.Client;
import com.example.repository.ClientRepo;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepo clientRepo;

    public ClientService(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    public void saveCleint(ClientRegistrationDto clientDto, Long userId) {

        String clientId = clientDto.getRegion() + clientDto.getNumberBank() + String.format("%08d", userId);

        Client savedClient = Client.builder()
                .clientId(clientId)
                .userId(userId)
                .firstName(clientDto.getFirstName())
                .middleName(clientDto.getMiddleName())
                .lastName(clientDto.getLastName())
                .dateOfBirth(clientDto.getDateOfBirth())
                .documentType(clientDto.getDocumentType())
                .documentId(clientDto.getDocumentId())
                .documentPrefix(clientDto.getDocumentPrefix())
                .documentSuffix(clientDto.getDocumentSuffix())
                .build();

        clientRepo.save(savedClient);
    }
}
