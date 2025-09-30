package com.example.repository;

import com.example.entity.ProductRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRegistryRepo extends JpaRepository<ProductRegistry, Long> {
    List<ProductRegistry> findByClientId(String clientId);
}
