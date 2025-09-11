package com.xp.guardian.service;

import com.xp.guardian.dto.ClientDTO;
import com.xp.guardian.dto.ClientDetailsDTO;
import com.xp.guardian.dto.CreateClientDTO;
import com.xp.guardian.exception.ClientNotFoundException;
import com.xp.guardian.model.Client;
import com.xp.guardian.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Transactional
    public ClientDTO createClient(CreateClientDTO dto) {
        Client client = new Client();
        client.setName(dto.name());
        client.setEmail(dto.email());
        client.setBalance(dto.initialBalance());

        Client savedClient = clientRepository.save(client);
        return ClientDTO.fromEntity(savedClient);
    }

    @Transactional(readOnly = true)
    public ClientDetailsDTO findClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException("Cliente com ID " + id + " não encontrado."));
        return ClientDetailsDTO.fromEntity(client);
    }
}