package com.example.dto.mapstruct;

import com.example.dto.ClientDto;
import com.example.entity.Client;
import org.mapstruct.Mapper;

public interface ClientMapper {

    Client clientToClientDto(ClientDto client);
    ClientDto clientDtoToClient(Client client);

}
