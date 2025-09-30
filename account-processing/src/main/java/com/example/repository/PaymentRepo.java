package com.example.repository;

import com.example.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Long> {
    Optional<Payment> findFirstByAccountIdAndExpiredFalseOrderByPaymentDateAsc(Long id);

    List<Payment> findByAccountIdAndPayedAtIsNull(Long id);
}
