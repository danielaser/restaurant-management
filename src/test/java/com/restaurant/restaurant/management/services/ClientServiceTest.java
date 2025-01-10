package com.restaurant.restaurant.management.services;

import com.restaurant.restaurant.management.models.Client;
import com.restaurant.restaurant.management.repositories.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    private Client client;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        client = new Client();
        client.setIdClient(1L);
        client.setClientName("John");
        client.setEmail("john@gmail.com");
        client.setPhoneNumber("12345678");
        client.setAddress("calle sol");
        client.setRegistrationDate(LocalDate.now());
        client.setFrequentUser(true);
        client.setVIP(false);
    }

    @Test
    @DisplayName("Agregar cliente exitosamente")
    void addClient() {
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        clientService.addClient(client);

        verify(clientRepository).save(client);
    }

    @Test
    @DisplayName("Obtener todos los clientes")
    void getAllClients() {
        when(clientRepository.findAll()).thenReturn(List.of(client));

        List<Client> clients = clientService.getAllClients();

        assertNotNull(clients);
        assertEquals(1, clients.size());
        assertEquals(client.getIdClient(), clients.get(0).getIdClient());
        verify(clientRepository).findAll();
    }

    @Test
    @DisplayName("Obtener cliente por id")
    void getClient() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));

        Optional<Client> result = clientService.getClient(1L);

        assertTrue(result.isPresent());
        assertEquals(client.getIdClient(), result.get().getIdClient());
        verify(clientRepository).findById(1L);
    }

    @Test
    @DisplayName("Actualizar cliente exitosamente")
    void updateClient() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.of(client));
        when(clientRepository.save(any(Client.class))).thenReturn(client);

        Client updatedClient = new Client();
        updatedClient.setClientName("Dani");
        updatedClient.setEmail("dani@gmail.com");
        updatedClient.setPhoneNumber("12345654");
        updatedClient.setAddress("calle luna");
        updatedClient.setRegistrationDate(LocalDate.now());

        Client result = clientService.updateClient(1L, updatedClient);

        assertNotNull(result);
        assertEquals(updatedClient.getClientName(), result.getClientName());
        assertEquals(updatedClient.getEmail(), result.getEmail());
        verify(clientRepository).findById(1L);
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    @DisplayName("Actualizar cliente pero no se encontro")
    void updateClientNotFound() {
        when(clientRepository.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clientService.updateClient(1L, client);
        });

        assertEquals("El cliente con id: 1 no pudo ser actualizado", exception.getMessage());
        verify(clientRepository).findById(1L);
        verify(clientRepository, never()).save(any(Client.class));
    }

    @Test
    @DisplayName("Eliminar cliente")
    void deleteClient() {
        doNothing().when(clientRepository).deleteById(anyLong());

        clientService.deleteClient(1L);

        verify(clientRepository).deleteById(1L);
    }
}
