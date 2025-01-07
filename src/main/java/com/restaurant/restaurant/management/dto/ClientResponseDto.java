package com.restaurant.restaurant.management.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class ClientResponseDto {
    private Long idClient;
    private String clientName;
    private String email;
    private String phoneNumber;
    private String address;
    private LocalDate registrationDate;
    private boolean frequentUser;
    private boolean isVIP;
}







