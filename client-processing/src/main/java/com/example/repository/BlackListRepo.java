package com.example.repository;

import com.example.entity.BlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlackListRepo  extends JpaRepository<BlackList, Long> {
    Optional<BlackList> findByFirstNameAndLastName(String firstName, String lastName);
}
