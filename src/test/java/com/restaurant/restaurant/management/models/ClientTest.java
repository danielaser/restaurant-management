package com.restaurant.restaurant.management.models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    @DisplayName("Crear cliente con constructor completo")
    void createClientWithFullConstructor() {
        LocalDate registrationDate = LocalDate.of(2024, 1, 1);
        Client client = new Client(1L, "Dani", "dani@example.com", "123456789", "Some Address", registrationDate, true);

        assertNotNull(client);
        assertEquals(1L, client.getIdClient());
        assertEquals("Dani", client.getClientName());
        assertEquals("dani@example.com", client.getEmail());
        assertEquals("123456789", client.getPhoneNumber());
        assertEquals("Some Address", client.getAddress());
        assertEquals(registrationDate, client.getRegistrationDate());
        assertTrue(client.isVIP());
    }

}