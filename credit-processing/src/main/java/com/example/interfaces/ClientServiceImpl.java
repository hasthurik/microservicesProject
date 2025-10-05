package com.example.interfaces;


import com.example.dto.ClientInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms1-client", url = "http://:8081/clients/", configuration = FeignClientsConfiguration.class)
@Component
public interface ClientServiceImpl extends ClientService {

    @Override
    @GetMapping("/clients/{clientId}")
    ClientInfoDto getClientInfo(@PathVariable("clientId") String clientId);
}
