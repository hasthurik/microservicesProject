package com.example.controller;

import com.example.dto.ClientInfoDto;
import com.example.service.ClientInfoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientInfoController {

    private final ClientInfoService clientInfoService;

    public ClientInfoController(ClientInfoService clientInfoService) {
        this.clientInfoService = clientInfoService;
    }

    @GetMapping("/clientsInfo/{id}")
    public ResponseEntity<ClientInfoDto> getClientInfo(@PathVariable("id") String clientId) {
        return new ResponseEntity<>(clientInfoService.getData(clientId), HttpStatus.OK);
    }
}
