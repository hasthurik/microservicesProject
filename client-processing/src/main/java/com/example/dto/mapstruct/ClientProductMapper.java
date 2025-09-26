package com.example.dto.mapstruct;

import com.example.dto.ClientProductDto;
import com.example.entity.ClientProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClientProductMapper {

    @Mapping(target = "productKey", ignore = true) // Заполняется отдельно
    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "openDate", source = "openDate")
    @Mapping(target = "status", source = "status")
    ClientProductDto toDto(ClientProduct clientProduct);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "clientId", source = "clientId")
    @Mapping(target = "productId", source = "productId")
    @Mapping(target = "openDate", source = "openDate")
    @Mapping(target = "closeDate", ignore = true) // Нет в DTO
    @Mapping(target = "status", source = "status")
    ClientProduct toEntity(ClientProductDto clientProductDto);

}
