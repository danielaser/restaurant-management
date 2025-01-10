package com.restaurant.restaurant.management.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ReservationTest {

    @Test
    @DisplayName("Crear Reservation con constructor completo")
    void createReservationWithFullConstructor() {
        Client client = new Client();
        client.setIdClient(1L);
        client.setClientName("Dani");

        LocalDate reservationDate = LocalDate.now();
        Integer numberOfPeople = 4;
        Double price = 100.50;

        Reservation reservation = new Reservation(1L, reservationDate, numberOfPeople, client, price);

        assertNotNull(reservation);
        assertEquals(1L, reservation.getIdReservation());
        assertEquals(reservationDate, reservation.getDateTime());
        assertEquals(numberOfPeople, reservation.getNumberOfPeople());
        assertEquals(client, reservation.getClient());
        assertEquals(price, reservation.getPrice());
    }
}