package com.restaurant.restaurant.management.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter

public class ReservationResponseDto {
    private Long idReservation;
    private LocalDateTime dateTime;
    private Integer numberOfPeople;
    private String clientName;
}
