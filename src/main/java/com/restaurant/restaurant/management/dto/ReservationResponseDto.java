package com.restaurant.restaurant.management.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter

public class ReservationResponseDto {
    private Long idReservation;
    private LocalDate dateTime;
    private Integer numberOfPeople;
    private String clientName;
    private Double price;
}
