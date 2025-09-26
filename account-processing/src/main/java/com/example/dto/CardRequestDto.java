package com.example.dto;

import com.example.enums.PaymentSystem;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CardRequestDto {

    private Long accountId;

    @JsonProperty("clientId")
    private String clientId;

    private PaymentSystem paymentSystem;

}
