package com.restaurant.restaurant.management.controllers;

import com.restaurant.restaurant.management.dto.ClientResponseDto;
import com.restaurant.restaurant.management.dtoConverter.ClientMapper;
import com.restaurant.restaurant.management.models.Client;
import com.restaurant.restaurant.management.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping
    public ResponseEntity<String> addClient(@RequestBody ClientResponseDto clientResponseDto){
        Client client = ClientMapper.toEntity(clientResponseDto);
        clientService.addClient(client);
        return ResponseEntity.ok("Cliente agregado exitosamente.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDto> getClient(@PathVariable Long id){
        return clientService.getClient(id)
                .map(client -> ResponseEntity.ok(ClientMapper.toDto(client)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDto>> getAllClients(){
        List<Client> clients = clientService.getAllClients();
        List<ClientResponseDto> response = clients.stream()
                .map(ClientMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDto> updateClient(@PathVariable Long id, @RequestBody ClientResponseDto clientResponseDto) {
        try {
            Client updated = ClientMapper.toEntity(clientResponseDto);
            return ResponseEntity.ok(ClientMapper.toDto(updated));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id){
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}
