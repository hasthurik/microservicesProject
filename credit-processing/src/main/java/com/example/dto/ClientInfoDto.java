package com.example.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClientInfoDto {
    private String firstName;
    private String middleName;
    private String lastName;
    private String documentNumber;
}
