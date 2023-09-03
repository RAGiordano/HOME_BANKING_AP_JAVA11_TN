package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ClientServiceImplement implements ClientService {
    // -------------------- Attributes --------------------
    @Autowired
    private ClientRepository clientRepository;


    // -------------------- Methods --------------------
    @Override
    public List<ClientDTO> findAllClients() {
        return clientRepository.findAll().stream()
                        .map(client -> new ClientDTO(client))
                        .collect(toList());
    }

    @Override
    public ClientDTO findClientById(Long id){
        return clientRepository.findById(id)
                .map(client -> new ClientDTO(client))
                .orElse(null);
    }

    @Override
    public Client findClientByEmail(String email) {
        return clientRepository.findByEmail(email);
    }

    @Override
    public void saveClient(Client client) {
        clientRepository.save(client);
    }
}
