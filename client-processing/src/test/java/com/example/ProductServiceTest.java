package com.example;

import com.example.dto.ProductDto;
import com.example.dto.mapstruct.ProductMapper;
import com.example.entity.Product;
import com.example.enums.ProductKey;
import com.example.repository.ProductRepo;
import com.example.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepo productRepo;

    @Mock
    private ProductMapper productMapper;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductDto dto;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setKey(ProductKey.AC);
        product.setName("Test Product");

        dto = new ProductDto();
        dto.setKey(ProductKey.AC);
        dto.setName("Test Product");
    }

    @Test
    void saveProduct_shouldSaveTwiceAndReturnSavedEntity() {
        when(productMapper.toEntity(dto)).thenReturn(product);
        when(productRepo.save(any(Product.class))).thenAnswer(invocation -> {
            Product p = invocation.getArgument(0);
            if (p.getId() == null) p.setId(1L);
            return p;
        });

        Product result = productService.saveProduct(dto);

        assertThat(result.getProductId()).isEqualTo("AC1");
        verify(productRepo, times(2)).save(any(Product.class));
        verify(productMapper).toEntity(dto);
    }


    @Test
    void deleteById_shouldReturnOk_whenExists() {
        when(productRepo.existsById(1L)).thenReturn(true);

        ResponseEntity<String> response = productService.deleteById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(productRepo).deleteById(1L);
    }

    @Test
    void deleteById_shouldReturnNotFound_whenNotExists() {
        when(productRepo.existsById(1L)).thenReturn(false);

        ResponseEntity<String> response = productService.deleteById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(productRepo, never()).deleteById(any());
    }

    @Test
    void updateProduct_shouldReturnUpdated_whenExists() {
        when(productRepo.findById(1L)).thenReturn(Optional.of(product));
        when(productRepo.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        dto.setCreateDate(LocalDate.now());
        ResponseEntity<Product> response = productService.updateProduct(dto, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo(dto.getName());
        assertThat(response.getBody().getProductId()).isEqualTo("AC1");
        verify(productRepo).save(product);
    }

    @Test
    void updateProduct_shouldReturnNotFound_whenNoProduct() {
        when(productRepo.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<Product> response = productService.updateProduct(dto, 1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(productRepo, never()).save(any());
    }
}

