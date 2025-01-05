package com.restaurant.restaurant.management.services;

import com.restaurant.restaurant.management.models.Reservation;
import com.restaurant.restaurant.management.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation addReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservation(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation updateReservation(Long id, Reservation reservationUpdated) {
        return reservationRepository.findById(id).map(existingReservation -> {
            existingReservation.setDateTime(reservationUpdated.getDateTime());
            existingReservation.setNumberOfPeople(reservationUpdated.getNumberOfPeople());
            return reservationRepository.save(existingReservation);
        }).orElseThrow(() -> new RuntimeException("Reserva con ID: " + id + " no encontrada"));
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
