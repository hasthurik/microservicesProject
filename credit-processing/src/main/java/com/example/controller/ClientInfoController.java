package com.example.controller;

import com.example.dto.ClientInfoDto;
import com.example.service.ClientInfoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientInfoController {

    private final ClientInfoService clientInfoService;

    public ClientInfoController(ClientInfoService clientInfoService) {
        this.clientInfoService = clientInfoService;
    }

    @GetMapping("/")
    public ResponseEntity<ClientInfoDto> getClientInfo() {
        return null;
    }

}
