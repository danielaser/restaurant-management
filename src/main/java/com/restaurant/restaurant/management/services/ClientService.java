package com.restaurant.restaurant.management.services;

import com.restaurant.restaurant.management.models.Client;
import com.restaurant.restaurant.management.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public void addClient(Client client){
        clientRepository.save(client);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClient(Long id) {
        return clientRepository.findById(id);
    }

    public Client updateClient(Long id, Client clientUpdated) {
        return clientRepository.findById(id).map(x -> {
            x.setClientName(clientUpdated.getClientName());
            x.setEmail(clientUpdated.getEmail());
            x.setPhoneNumber(clientUpdated.getPhoneNumber());
            x.setAddress(clientUpdated.getAddress());
            x.setRegistrationDate(clientUpdated.getRegistrationDate());
            return clientRepository.save(x);
        }).orElseThrow(() -> new RuntimeException("El cliente con id: " + id + " no pudo ser actualizado"));
    }

    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }
}
