package com.example.repository;

import com.example.entity.ClientProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientProductRepo extends JpaRepository<ClientProduct, Long> {
}
