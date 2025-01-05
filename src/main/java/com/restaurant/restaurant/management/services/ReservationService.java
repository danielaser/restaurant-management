package com.restaurant.restaurant.management.services;

import com.restaurant.restaurant.management.repositories.OrderRepository;
import com.restaurant.restaurant.management.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

//    public ReservationDto createReservation(ReservationDto reservationDto) {
//        Reservation reservation = ReservationMapper.toEntity(reservationDto);
//        return ReservationMapper.toDto(reservationRepository.save(reservation));
//    }
}
