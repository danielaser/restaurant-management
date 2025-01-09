package com.restaurant.restaurant.management.controllers;

import com.restaurant.restaurant.management.dto.ReservationResponseDto;
import com.restaurant.restaurant.management.models.Client;
import com.restaurant.restaurant.management.models.Reservation;
import com.restaurant.restaurant.management.services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ReservationControllerTest {

    private WebTestClient webTestClient;
    private ReservationService reservationService;
    private Reservation reservation;
    private ReservationResponseDto reservationResponseDto;
    private Client client;

    @BeforeEach
    void setup() {
        reservationService = mock(ReservationService.class);
        webTestClient = WebTestClient.bindToController(new ReservationController(reservationService, null)).build();

        client = new Client();
        client.setIdClient(1L);
        client.setClientName("Jane Doe");
        client.setEmail("janedoe@example.com");
        client.setVIP(false);

        reservation = new Reservation();
        reservation.setIdReservation(1L);
        reservation.setDateTime(LocalDate.now());
        reservation.setNumberOfPeople(4);
        reservation.setClient(client);
        reservation.setPrice(100.0);

        reservationResponseDto = new ReservationResponseDto();
        reservationResponseDto.setIdReservation(1L);
        reservationResponseDto.setDateTime(LocalDate.now());
        reservationResponseDto.setNumberOfPeople(4);
        reservationResponseDto.setClientName("Jane Doe");
        reservationResponseDto.setPrice(100.0);
    }

    @Test
    @DisplayName("Agregar reserva")
    void addReservation() {
        when(reservationService.getClientByName(any(String.class))).thenReturn(Optional.of(client));
        when(reservationService.addReservation(any(Reservation.class), anyLong())).thenReturn(reservation);
        when(reservationService.applyPricingStrategy(any(Reservation.class))).thenReturn(100.0);

        webTestClient.post()
                .uri("/api/reservations/{clientName}", "Jane Doe")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(reservationResponseDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ReservationResponseDto.class)
                .value(dto -> {
                    assertEquals(reservationResponseDto.getIdReservation(), dto.getIdReservation());
                    assertEquals(reservationResponseDto.getClientName(), dto.getClientName());
                    assertEquals(reservationResponseDto.getPrice(), dto.getPrice());
                });

        verify(reservationService).getClientByName(any(String.class));
        verify(reservationService).addReservation(any(Reservation.class), anyLong());
        verify(reservationService).applyPricingStrategy(any(Reservation.class));
    }

    @Test
    @DisplayName("Obtener reserva por ID")
    void getReservation() {
        when(reservationService.getReservation(anyLong())).thenReturn(Optional.of(reservation));

        webTestClient.get()
                .uri("/api/reservations/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ReservationResponseDto.class)
                .value(dto -> {
                    assertEquals(reservationResponseDto.getIdReservation(), dto.getIdReservation());
                    assertEquals(reservationResponseDto.getClientName(), dto.getClientName());
                });

        verify(reservationService).getReservation(anyLong());
    }

    @Test
    @DisplayName("Obtener todas las reservas")
    void getAllReservations() {
        when(reservationService.getAllReservations()).thenReturn(List.of(reservation));

        webTestClient.get()
                .uri("/api/reservations")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ReservationResponseDto.class)
                .hasSize(1)
                .value(list -> {
                    assertEquals(reservationResponseDto.getIdReservation(), list.get(0).getIdReservation());
                    assertEquals(reservationResponseDto.getClientName(), list.get(0).getClientName());
                });

        verify(reservationService).getAllReservations();
    }

    @Test
    @DisplayName("Actualizar reserva")
    void updateReservation() {
        when(reservationService.updateReservation(anyLong(), any(Reservation.class))).thenReturn(reservation);

        webTestClient.put()
                .uri("/api/reservations/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(reservationResponseDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ReservationResponseDto.class)
                .value(dto -> {
                    assertEquals(reservationResponseDto.getIdReservation(), dto.getIdReservation());
                    assertEquals(reservationResponseDto.getClientName(), dto.getClientName());
                });

        verify(reservationService).updateReservation(anyLong(), any(Reservation.class));
    }

    @Test
    @DisplayName("Eliminar reserva")
    void deleteReservation() {
        doNothing().when(reservationService).deleteReservation(anyLong());

        webTestClient.delete()
                .uri("/api/reservations/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        verify(reservationService).deleteReservation(anyLong());
    }
}