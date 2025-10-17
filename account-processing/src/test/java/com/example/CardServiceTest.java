package com.example;

import com.example.dto.CardRequestDto;
import com.example.entity.Account;
import com.example.entity.Card;
import com.example.enums.AccountStatus;
import com.example.enums.PaymentSystem;
import com.example.repository.AccountRepo;
import com.example.repository.CardRepo;
import com.example.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepo cardRepo;

    @Mock
    private AccountRepo accountRepo;

    @InjectMocks
    private CardService cardService;

    private Account activeAccount;
    private CardRequestDto cardRequest;

    @BeforeEach
    void setUp() {
        activeAccount = new Account();
        activeAccount.setId(1L);
        activeAccount.setClientId("client1");
        activeAccount.setStatus(AccountStatus.ACTIVE);

        cardRequest = new CardRequestDto();
        cardRequest.setClientId("client1");
        cardRequest.setPaymentSystem(PaymentSystem.VISA);
    }

    @Test
    void createCard_shouldSaveCard_whenAccountIsActive() {
        when(accountRepo.findByClientId("client1")).thenReturn(Optional.of(activeAccount));
        when(cardRepo.existsByCardId(anyString())).thenReturn(false);

        cardService.createCard(cardRequest);

        verify(cardRepo, times(1)).save(any(Card.class));
    }

    @Test
    void createCard_shouldThrowException_whenAccountNotFound() {
        when(accountRepo.findByClientId("client1")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> cardService.createCard(cardRequest));
    }

    @Test
    void createCard_shouldThrowException_whenAccountNotActive() {
        activeAccount.setStatus(AccountStatus.BLOCKED);
        when(accountRepo.findByClientId("client1")).thenReturn(Optional.of(activeAccount));

        assertThrows(RuntimeException.class, () -> cardService.createCard(cardRequest));
    }

    @Test
    void generateUniqueCardId_shouldGenerateNewId_whenExists() {
        when(cardRepo.existsByCardId("CARD_000001")).thenReturn(true);
        when(cardRepo.existsByCardId("CARD_000002")).thenReturn(false);

        String cardId = cardService.generateUniqueCardId();

        assertEquals("CARD_000002", cardId);
    }
}

