package com.example.controller;

import com.example.dto.ClientProductDto;
import com.example.entity.ClientProduct;
import com.example.service.ClientProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client-product")
public class ClientProductController {

    private final ClientProductService service;

    public ClientProductController(ClientProductService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<ClientProduct>> getProductById(@PathVariable Long id) {
        return service.getById(id);
    }

    @GetMapping
    public ResponseEntity<List<ClientProduct>> getAllProducts() {
        return service.getAll();
    }


    @PostMapping
    public ResponseEntity<ClientProductDto> create(@RequestBody ClientProductDto product) {
        return service.create(product);
    }

    @PutMapping
    public ResponseEntity<ClientProduct> update(@RequestBody ClientProduct product) {
        return service.update(product);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClientProduct(@PathVariable Long id) {
        return service.deleteById(id);
    }
}
