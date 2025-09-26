package com.example.controller;

import com.example.dto.CardRequestDto;
import com.example.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<String> createCardRequest(@RequestBody CardRequestDto card) {
        System.out.println("CardRequestDtoMS2 Controller: " + card);
        return cardService.request(card);
    }

}
