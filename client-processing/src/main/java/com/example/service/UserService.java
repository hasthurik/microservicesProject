package com.example.service;

import com.example.dto.ClientRegistrationDto;
import com.example.entity.User;
import com.example.repository.UserRepo;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public Long saveUser(ClientRegistrationDto clientDto) {

        User savedUser = User.builder()
                .login(clientDto.getLogin())
                .email(clientDto.getEmail())
                .password(encoder.encode(clientDto.getPassword()))
                .build();

        userRepo.save(savedUser);
        return savedUser.getId();
    }
}
