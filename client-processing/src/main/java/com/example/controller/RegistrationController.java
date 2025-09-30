package com.example.controller;

import com.example.dto.ClientRegistrationDto;
import com.example.repository.BlackListRepo;
import com.example.service.ClientService;
import com.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/registration")
public class RegistrationController {
    private final UserService userService;
    private final ClientService clientService;
    private final BlackListRepo blackListRepo;

    public RegistrationController(UserService userService, ClientService clientService, BlackListRepo blackListRepo) {
        this.userService = userService;
        this.clientService = clientService;
        this.blackListRepo = blackListRepo;
    }

    @PostMapping()
    public ResponseEntity<String> registerClient(@RequestBody ClientRegistrationDto clientDto) {

        if (blackListRepo.findByFirstNameAndLastName(clientDto.getFirstName(), clientDto.getLastName()).isPresent()) {
            return new ResponseEntity<>("Client - BANNED!!!!!", HttpStatus.CONFLICT);
        }
        Long userId = userService.saveUser(clientDto);
        clientService.saveCleint(clientDto, userId);

        return new ResponseEntity<>("Client is registered", HttpStatus.CREATED);

    }
}
