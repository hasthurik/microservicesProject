package com.example;


import com.example.dto.ClientProductDto;
import com.example.dto.mapstruct.ClientProductMapper;
import com.example.entity.ClientProduct;
import com.example.enums.ProductKey;
import com.example.kafka.KafkaEventProducer;
import com.example.repository.ClientProductRepo;
import com.example.service.ClientProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ClientProductServiceTest {

    @Mock
    private ClientProductRepo repo;

    @Mock
    private KafkaEventProducer producer;

    @Mock
    private ClientProductMapper mapper;

    @InjectMocks
    private ClientProductService service;

    private ClientProduct entity;
    private ClientProductDto dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        entity = new ClientProduct();
        dto = new ClientProductDto();
        dto.setProductKey(ProductKey.IPO);
    }

    @Test
    void getById_shouldReturnOk_whenExists() {
        when(repo.existsById(1L)).thenReturn(true);
        when(repo.findById(1L)).thenReturn(Optional.of(entity));

        ResponseEntity<Optional<ClientProduct>> response = service.getById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains(entity);
    }

    @Test
    void getById_shouldReturnNotFound_whenNotExists() {
        when(repo.existsById(1L)).thenReturn(false);

        ResponseEntity<Optional<ClientProduct>> response = service.getById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void create_shouldSaveEntity_andSendCreditProduct_whenProductKeyIPO() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repo.save(any(ClientProduct.class))).thenReturn(entity);

        ResponseEntity<ClientProductDto> response = service.create(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isEqualTo(dto);
        verify(repo).save(entity);
        verify(producer).sendClientCreditProducts(dto);
        verify(producer, never()).sendClientProduct(dto);
    }

    @Test
    void create_shouldSendClientProduct_whenKeyDC() {
        dto.setProductKey(ProductKey.DC);
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repo.save(any(ClientProduct.class))).thenReturn(entity);

        service.create(dto);

        verify(producer).sendClientProduct(dto);
        verify(producer, never()).sendClientCreditProducts(dto);
    }

    @Test
    void update_shouldReturnOk_andSaveEntity() {
        when(repo.save(entity)).thenReturn(entity);

        ResponseEntity<ClientProduct> response = service.update(entity);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(repo).save(entity);
    }

    @Test
    void getAll_shouldReturnList() {
        List<ClientProduct> list = List.of(entity);
        when(repo.findAll()).thenReturn(list);

        ResponseEntity<List<ClientProduct>> response = service.getAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).containsExactly(entity);
    }

    @Test
    void deleteById_shouldDelete_whenExists() {
        when(repo.existsById(1L)).thenReturn(true);

        ResponseEntity<String> response = service.deleteById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(repo).deleteById(1L);
    }

    @Test
    void deleteById_shouldReturnNotFound_whenNotExists() {
        when(repo.existsById(1L)).thenReturn(false);

        ResponseEntity<String> response = service.deleteById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(repo, never()).deleteById(any());
    }
}
