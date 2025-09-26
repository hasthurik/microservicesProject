package com.example.dto.mapstruct;

import com.example.dto.ProductDto;
import com.example.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true) // Игнорируем id при создании
    @Mapping(target = "productId", ignore = true) // productId должен генерироваться отдельно
    @Mapping(target = "name", source = "name")
    @Mapping(target = "key", source = "key")
    @Mapping(target = "createDate", source = "createDate")
    Product toEntity(ProductDto productDto);

}
