package com.restaurant.restaurant.management.services;

import com.restaurant.restaurant.management.models.Client;
import com.restaurant.restaurant.management.models.Reservation;
import com.restaurant.restaurant.management.repositories.ReservationRepository;
import com.restaurant.restaurant.management.repositories.ClientRepository;
import com.restaurant.restaurant.management.strategy.PricingStrategy;
import com.restaurant.restaurant.management.strategy.RegularPricingStrategy;
import com.restaurant.restaurant.management.strategy.VIPPricingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, ClientRepository clientRepository) {
        this.reservationRepository = reservationRepository;
        this.clientRepository = clientRepository;
    }

    public Optional<Client> getClientByName(String clientName) {
        return clientRepository.findAll().stream()
                .filter(client -> client.getClientName().equalsIgnoreCase(clientName))
                .findFirst();
    }

    public Reservation addReservation(Reservation reservation, Long clientId) {
        Optional<Client> client = clientRepository.findById(clientId);
        if (client.isPresent()) {
            reservation.setClient(client.get());
            return reservationRepository.save(reservation);
        } else {
            throw new RuntimeException("El cliente con ID: " + clientId + " no fue encontrado");
        }
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

    public double applyPricingStrategy(Reservation reservation) {
        PricingStrategy pricingStrategy;
        if (reservation.getClient().isVIP()) pricingStrategy = new VIPPricingStrategy();
        else pricingStrategy = new RegularPricingStrategy();
        return pricingStrategy.calculatePrice(reservation);
    }
}
