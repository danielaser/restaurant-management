package com.restaurant.restaurant.management.services;

import com.restaurant.restaurant.management.models.Client;
import com.restaurant.restaurant.management.models.Reservation;
import com.restaurant.restaurant.management.repositories.ClientRepository;
import com.restaurant.restaurant.management.repositories.ReservationRepository;
import com.restaurant.restaurant.management.strategy.RegularPricingStrategy;
import com.restaurant.restaurant.management.strategy.VIPPricingStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    private ReservationService reservationService;
    private ReservationRepository reservationRepository;
    private ClientRepository clientRepository;
    private Client client;
    private Reservation reservation;

    @BeforeEach
    void setup() {
        reservationRepository = mock(ReservationRepository.class);
        clientRepository = mock(ClientRepository.class);
        reservationService = new ReservationService(reservationRepository, clientRepository);

        client = new Client();
        client.setIdClient(1L);
        client.setClientName("Dani");
        client.setVIP(false);

        reservation = new Reservation();
        reservation.setIdReservation(1L);
        reservation.setDateTime(LocalDate.now());
        reservation.setNumberOfPeople(4);
        reservation.setClient(client);
    }

    @Test
    @DisplayName("Agregar una reserva con cliente valido")
    void addReservationWithValidClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation savedReservation = reservationService.addReservation(reservation, 1L);

        assertNotNull(savedReservation);
        assertEquals(client, savedReservation.getClient());
        verify(clientRepository).findById(1L);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    @DisplayName("Error al agregar reserva con cliente no encontrado")
    void addReservationWithInvalidClient() {
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                reservationService.addReservation(reservation, 1L));

        assertEquals("El cliente con id: 1 no fue encontrado", exception.getMessage());
        verify(clientRepository).findById(1L);
        verify(reservationRepository, never()).save(any(Reservation.class));
    }

    @Test
    @DisplayName("Obtener todas las reservas")
    void getAllReservations() {
        when(reservationRepository.findAll()).thenReturn(List.of(reservation));

        List<Reservation> reservations = reservationService.getAllReservations();

        assertNotNull(reservations);
        assertEquals(1, reservations.size());
        verify(reservationRepository).findAll();
    }

    @Test
    @DisplayName("Obtener reserva por id")
    void getReservationById() {
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        Optional<Reservation> foundReservation = reservationService.getReservation(1L);

        assertTrue(foundReservation.isPresent());
        assertEquals(reservation, foundReservation.get());
        verify(reservationRepository).findById(1L);
    }

    @Test
    @DisplayName("Actualizar reserva existente")
    void updateReservation() {
        Reservation updatedReservation = new Reservation();
        updatedReservation.setDateTime(LocalDate.now().plusDays(1));
        updatedReservation.setNumberOfPeople(5);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation result = reservationService.updateReservation(1L, updatedReservation);

        assertNotNull(result);
        assertEquals(5, result.getNumberOfPeople());
        verify(reservationRepository).findById(1L);
        verify(reservationRepository).save(any(Reservation.class));
    }

    @Test
    @DisplayName("Eliminar reserva")
    void deleteReservation() {
        doNothing().when(reservationRepository).deleteById(1L);

        reservationService.deleteReservation(1L);

        verify(reservationRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Aplicar estrategia de precios regular")
    void applyRegularPricingStrategy() {
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        reservation.setClient(client);
        double price = reservationService.applyPricingStrategy(reservation);

        assertTrue(price > 0);
        assertInstanceOf(RegularPricingStrategy.class, new RegularPricingStrategy());
    }

    @Test
    @DisplayName("Aplicar estrategia de precios VIP")
    void applyVIPPricingStrategy() {
        client.setVIP(true);
        reservation.setClient(client);
        double price = reservationService.applyPricingStrategy(reservation);

        assertTrue(price > 0);
        assertInstanceOf(VIPPricingStrategy.class, new VIPPricingStrategy());
    }

    @Test
    @DisplayName("Obtener cliente por nombre correctamente")
    void getClientByName() {
        String clientName = "Dani";
        when(clientRepository.findAll()).thenReturn(List.of(client));

        Optional<Client> foundClient = reservationService.getClientByName(clientName);

        assertTrue(foundClient.isPresent());
        assertEquals(clientName, foundClient.get().getClientName());
        verify(clientRepository).findAll();
    }

}