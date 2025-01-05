package com.restaurant.restaurant.management.services;

import com.restaurant.restaurant.management.dto.ClientResponseDto;
import com.restaurant.restaurant.management.dtoConverter.ClientMapper;
import com.restaurant.restaurant.management.models.Client;
import com.restaurant.restaurant.management.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

//    public ClientDto createClient(ClientDto clientDto) {
//        Client client = ClientMapper.toEntity(clientDto);
//        return ClientMapper.toDto(clientRepository.save(client));
//    }


    public List<ClientResponseDto> getAllClients() {
        return clientRepository.findAll().stream()
                .map(ClientMapper::toDto)
                .toList();
    }
}
