package com.xp.guardian.controller;

import com.xp.guardian.dto.ClientDTO;
import com.xp.guardian.dto.ClientDetailsDTO;
import com.xp.guardian.dto.CreateClientDTO;
import com.xp.guardian.service.ClientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody CreateClientDTO createClientDTO) {
        ClientDTO newClient = clientService.createClient(createClientDTO);
        return new ResponseEntity<>(newClient, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDetailsDTO> getClientById(@PathVariable Long id) {
        ClientDetailsDTO client = clientService.findClientById(id);
        return ResponseEntity.ok(client);
    }
}