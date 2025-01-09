package com.restaurant.restaurant.management.controllers;

import com.restaurant.restaurant.management.dto.ClientResponseDto;
import com.restaurant.restaurant.management.models.Client;
import com.restaurant.restaurant.management.services.ClientService;
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
import static org.mockito.Mockito.*;

class ClientControllerTest {

    private WebTestClient webTestClient;
    private ClientService clientService;
    private Client client;
    private ClientResponseDto clientResponseDto;

    @BeforeEach
    void setup() {
        clientService = mock(ClientService.class);
        webTestClient = WebTestClient.bindToController(new ClientController(clientService)).build();

        client = new Client();
        client.setIdClient(1L);
        client.setClientName("John Doe");
        client.setEmail("johndoe@example.com");
        client.setPhoneNumber("1234567890");
        client.setAddress("123 Main St");
        client.setRegistrationDate(LocalDate.now());
        client.setFrequentUser(true);
        client.setVIP(false);

        clientResponseDto = new ClientResponseDto();
        clientResponseDto.setIdClient(1L);
        clientResponseDto.setClientName("John Doe");
        clientResponseDto.setEmail("johndoe@example.com");
        clientResponseDto.setPhoneNumber("1234567890");
        clientResponseDto.setAddress("123 Main St");
        clientResponseDto.setRegistrationDate(LocalDate.now());
        clientResponseDto.setFrequentUser(true);
        clientResponseDto.setVIP(false);
    }

    @Test
    @DisplayName("Agregar cliente")
    void addClient() {
        doNothing().when(clientService).addClient(any(Client.class));

        webTestClient.post()
                .uri("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(clientResponseDto)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .value(message -> assertEquals("Cliente agregado exitosamente", message));

        verify(clientService).addClient(any(Client.class));
    }

    @Test
    @DisplayName("Obtener cliente por ID")
    void getClient() {
        when(clientService.getClient(anyLong())).thenReturn(Optional.of(client));

        webTestClient.get()
                .uri("/api/clients/{id}", 1L)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ClientResponseDto.class)
                .value(dto -> {
                    assertEquals(clientResponseDto.getIdClient(), dto.getIdClient());
                    assertEquals(clientResponseDto.getClientName(), dto.getClientName());
                    assertEquals(clientResponseDto.getEmail(), dto.getEmail());
                    assertEquals(clientResponseDto.getPhoneNumber(), dto.getPhoneNumber());
                });

        verify(clientService).getClient(anyLong());
    }

    @Test
    @DisplayName("Obtener todos los clientes")
    void getAllClients() {
        when(clientService.getAllClients()).thenReturn(List.of(client));

        webTestClient.get()
                .uri("/api/clients")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(ClientResponseDto.class)
                .hasSize(1)
                .value(list -> {
                    assertEquals(clientResponseDto.getIdClient(), list.get(0).getIdClient());
                    assertEquals(clientResponseDto.getClientName(), list.get(0).getClientName());
                });

        verify(clientService).getAllClients();
    }

    @Test
    @DisplayName("Actualizar cliente")
    void updateClient() {
        when(clientService.updateClient(anyLong(), any(Client.class))).thenReturn(client);

        webTestClient.put()
                .uri("/api/clients/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(clientResponseDto)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(ClientResponseDto.class)
                .value(dto -> {
                    assertEquals(clientResponseDto.getIdClient(), dto.getIdClient());
                    assertEquals(clientResponseDto.getClientName(), dto.getClientName());
                });

        verify(clientService).updateClient(anyLong(), any(Client.class));
    }

    @Test
    @DisplayName("Eliminar cliente")
    void deleteClient() {
        doNothing().when(clientService).deleteClient(anyLong());

        webTestClient.delete()
                .uri("/api/clients/{id}", 1L)
                .exchange()
                .expectStatus().isNoContent();

        verify(clientService).deleteClient(anyLong());
    }

    @Test
    @DisplayName("Error al agregar cliente")
    void addClientInternalServerError() {
        doThrow(new RuntimeException("Database error")).when(clientService).addClient(any(Client.class));

        webTestClient.post()
                .uri("/api/clients")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(clientResponseDto)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectBody(String.class)
                .value(message -> {
                    assertTrue(message.contains("Error al agregar el cliente:"));
                    assertTrue(message.contains("Database error"));
                });

        verify(clientService).addClient(any(Client.class));
    }

    @Test
    @DisplayName("Actualizar cliente pero cliente no encontrado")
    void updateClientNotFound() {
        when(clientService.updateClient(anyLong(), any(Client.class))).thenThrow(new RuntimeException("Client not found"));

        webTestClient.put()
                .uri("/api/clients/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(clientResponseDto)
                .exchange()
                .expectStatus().isNotFound();

        verify(clientService).updateClient(anyLong(), any(Client.class));
    }

}
