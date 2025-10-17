package com.example;

import com.example.dto.ClientCreditProductDto;
import com.example.dto.ClientInfoDto;
import com.example.entity.PaymentRegistry;
import com.example.entity.ProductRegistry;
import com.example.interfaces.ClientService;
import com.example.repository.PaymentScheduleRepo;
import com.example.repository.ProductRegistryRepo;
import com.example.service.CreditProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditProductServiceTest {

    @Mock
    private ProductRegistryRepo productRegistryRepo;

    @Mock
    private PaymentScheduleRepo paymentScheduleRepo;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private CreditProductService creditProductService;

    private ClientCreditProductDto dto;
    private ClientInfoDto clientInfo;
    private ProductRegistry existingProduct;

    @BeforeEach
    void setUp() {
        // DTO запроса кредита
        dto = new ClientCreditProductDto();
        dto.setClientId("client1");
        dto.setAccountId(1L);
        dto.setProducerId(1L);
        dto.setInterestRate(12.0);
        dto.setMountNumber(12);
        dto.setCreditAmount(BigDecimal.valueOf(5000));

        // Информация о клиенте
        clientInfo = new ClientInfoDto();
        clientInfo.setFirstName("Иван");
        clientInfo.setMiddleName("Иванович");
        clientInfo.setLastName("Иванов");
        clientInfo.setDocumentNumber("1234567890");

        // Существующий продукт
        existingProduct = ProductRegistry.builder()
                .id(1L)
                .clientId("client1")
                .build();

        // Общий мок clientService
        when(clientService.getClientInfo("client1")).thenReturn(clientInfo);
    }

    @Test
    void processCreditRequest_shouldSaveProductAndPayments_whenWithinLimitAndNoOverdue() {
        when(productRegistryRepo.findByClientId("client1")).thenReturn(List.of());

        when(paymentScheduleRepo.findByProductRegistryId(anyLong()))
                .thenReturn(List.of(
                        PaymentRegistry.builder()
                                .amount(BigDecimal.ZERO)
                                .expired(false)
                                .build()
                ));

        creditProductService.processCreditRequest(dto);

        verify(productRegistryRepo).save(any(ProductRegistry.class));
        verify(paymentScheduleRepo, atLeastOnce()).save(any(PaymentRegistry.class));
    }


    @Test
    void processCreditRequest_shouldReject_whenExceedsMaxCreditLimit() {
        when(productRegistryRepo.findByClientId("client1")).thenReturn(List.of(existingProduct));

        when(paymentScheduleRepo.findByProductRegistryId(1L)).thenReturn(
                List.of(PaymentRegistry.builder()
                        .amount(BigDecimal.valueOf(10000))
                        .expired(false)
                        .build())
        );

        creditProductService.processCreditRequest(dto);

        // Должен сохранить продукт, но не создавать платежи
        verify(productRegistryRepo).save(any(ProductRegistry.class));
        verify(paymentScheduleRepo, never()).save(any(PaymentRegistry.class));
    }

    @Test
    void processCreditRequest_shouldReject_whenHasOverduePayments() {
        when(productRegistryRepo.findByClientId("client1")).thenReturn(List.of(existingProduct));

        // Продукт с просроченным платежом
        when(paymentScheduleRepo.findByProductRegistryId(1L)).thenReturn(
                List.of(PaymentRegistry.builder()
                        .amount(BigDecimal.valueOf(1000))
                        .expired(true)  // просроченный
                        .build())
        );

        creditProductService.processCreditRequest(dto);

        verify(productRegistryRepo).save(any(ProductRegistry.class));
        verify(paymentScheduleRepo, never()).save(any(PaymentRegistry.class));
    }

    @Test
    void processCreditRequest_shouldThrow_whenClientInfoNotFound() {
        when(productRegistryRepo.findByClientId("client1")).thenReturn(List.of());
        when(clientService.getClientInfo("client1")).thenReturn(null);

        assertThrows(RuntimeException.class,
                () -> creditProductService.processCreditRequest(dto));
    }
}

