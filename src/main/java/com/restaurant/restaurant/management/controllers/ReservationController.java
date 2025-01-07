package com.restaurant.restaurant.management.controllers;

import com.restaurant.restaurant.management.dto.ReservationResponseDto;
import com.restaurant.restaurant.management.dtoConverter.ReservationMapper;
import com.restaurant.restaurant.management.models.Client;
import com.restaurant.restaurant.management.models.Reservation;
import com.restaurant.restaurant.management.repositories.ClientRepository;
import com.restaurant.restaurant.management.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ClientRepository clientRepository;

    @Autowired
    public ReservationController(ReservationService reservationService, ClientRepository clientRepository) {
        this.reservationService = reservationService;
        this.clientRepository = clientRepository;
    }

    @PostMapping("/{clientName}")
    public ResponseEntity<ReservationResponseDto> addReservation(@RequestBody ReservationResponseDto reservationResponseDto, @PathVariable String clientName) {
        Optional<Client> clientOptional = reservationService.getClientByName(clientName);
        if (!clientOptional.isPresent()) return ResponseEntity.notFound().build();
        Reservation reservation = ReservationMapper.toEntity(reservationResponseDto);
        reservation.setClient(clientOptional.get());
        double price = reservationService.applyPricingStrategy(reservation);
        reservation.setPrice(price);
        Reservation addedReservation = reservationService.addReservation(reservation, clientOptional.get().getIdClient());
        return ResponseEntity.ok(ReservationMapper.toDto(addedReservation));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> getReservation(@PathVariable Long id) {
        Optional<Reservation> reservation = reservationService.getReservation(id);
        return reservation.map(res -> ResponseEntity.ok(ReservationMapper.toDto(res)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ReservationResponseDto>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        List<ReservationResponseDto> response = reservations.stream()
                .map(ReservationMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDto> updateReservation(@PathVariable Long id, @RequestBody ReservationResponseDto reservationResponseDto) {
        Reservation reservation = ReservationMapper.toEntity(reservationResponseDto);
        reservation.setIdReservation(id);
        Reservation updatedReservation = reservationService.updateReservation(id, reservation);
        return ResponseEntity.ok(ReservationMapper.toDto(updatedReservation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
