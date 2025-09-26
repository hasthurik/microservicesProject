package com.example.dto;

import com.example.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClientRegistrationDto {

    // данные для User
    private String login;

    private String password;

    private String email;

    // данные для Client

    private String region;

    private String numberBank;


    private String firstName;

    private String middleName;

    private String lastName;

    private Date dateOfBirth;


    private DocumentType documentType;

    private String documentId;

    private String documentPrefix;

    private String documentSuffix;

}
