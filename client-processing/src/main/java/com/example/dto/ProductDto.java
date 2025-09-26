package com.example.dto;

import com.example.enums.ProductKey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private String name;

    private ProductKey key;

    private LocalDate createDate;

}
