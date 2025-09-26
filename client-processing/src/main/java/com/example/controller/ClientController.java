package com.example.controller;


import com.example.entity.Client;
import com.example.repository.ClientRepo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController {

    private final ClientRepo clientRepo;

    public ClientController(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    @GetMapping
    private List<Client> getClients() {
        return clientRepo.findAll();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Optional<Client>> getClients(@PathVariable Long id) {
        return new ResponseEntity<>(clientRepo.findById(id), HttpStatus.OK);
    }

}
