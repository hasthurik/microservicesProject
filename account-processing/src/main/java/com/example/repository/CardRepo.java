package com.example.repository;

import com.example.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CardRepo extends JpaRepository<Card, Long> {
    Boolean existsByCardId(String cardNumber);
}
