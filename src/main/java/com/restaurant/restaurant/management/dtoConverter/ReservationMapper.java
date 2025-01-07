package com.restaurant.restaurant.management.dtoConverter;

import com.restaurant.restaurant.management.dto.ReservationResponseDto;
import com.restaurant.restaurant.management.models.Client;
import com.restaurant.restaurant.management.models.Reservation;

public class ReservationMapper {
    public static ReservationResponseDto toDto(Reservation reservation) {
        ReservationResponseDto dto = new ReservationResponseDto();
        dto.setIdReservation(reservation.getIdReservation());
        dto.setDateTime(reservation.getDateTime());
        dto.setNumberOfPeople(reservation.getNumberOfPeople());
        dto.setClientName(reservation.getClient().getClientName());
        dto.setPrice(reservation.getPrice());
        return dto;
    }

    public static Reservation toEntity(ReservationResponseDto dto) {
        Reservation reservation = new Reservation();
        reservation.setIdReservation(dto.getIdReservation());
        reservation.setDateTime(dto.getDateTime());
        reservation.setNumberOfPeople(dto.getNumberOfPeople());
        reservation.setPrice(dto.getPrice());
        return reservation;
    }
}
