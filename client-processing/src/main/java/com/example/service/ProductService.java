package com.example.service;

import com.example.dto.ProductDto;
import com.example.dto.mapstruct.ProductMapper;
import com.example.entity.Product;
import com.example.repository.ProductRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepo productRepo;
    private final ProductMapper productMapper;

    public ProductService(ProductRepo productRepo, ProductMapper productMapper) {
        this.productRepo = productRepo;
        this.productMapper = productMapper;
    }


    public Product saveProduct(ProductDto product) {
        Product savedProduct = productMapper.toEntity(product);
        savedProduct.setProductId("000");
        productRepo.save(savedProduct);
        savedProduct.setProductId(savedProduct.getKey() + savedProduct.getId().toString());

        return productRepo.save(savedProduct);
    }


    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }


    public Optional<Product> getProductById(Long id) {
        return productRepo.findById(id);
    }

    public ResponseEntity<String> deleteById(Long id) {
        if (productRepo.existsById(id)) {
            productRepo.deleteById(id);
            return new ResponseEntity<>("Product deleted!", HttpStatus.OK);
        }
        return new ResponseEntity<>("Product not found!", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Product> updateProduct(ProductDto productDto, Long id) {

        Optional<Product> existingProductOpt = productRepo.findById(id);
        if (existingProductOpt.isPresent()) {
            Product existingProduct = existingProductOpt.get();

            existingProduct.setName(productDto.getName());
            existingProduct.setKey(productDto.getKey());
            existingProduct.setProductId(productDto.getKey() + id.toString());
            existingProduct.setCreateDate(productDto.getCreateDate());

            return new ResponseEntity<>(productRepo.save(existingProduct), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
