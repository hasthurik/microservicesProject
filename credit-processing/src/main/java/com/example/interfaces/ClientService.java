package com.example.interfaces;


import com.example.dto.ClientInfoDto;
import org.springframework.web.bind.annotation.PathVariable;

public interface ClientService {
    ClientInfoDto getClientInfo(@PathVariable("clientId") String clientId);
}
