package com.xp.guardian.controller;

import com.xp.guardian.dto.TransactionRequestDTO;
import com.xp.guardian.dto.TransactionResponseDTO;
import com.xp.guardian.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<TransactionResponseDTO> processTransaction(@Valid @RequestBody TransactionRequestDTO requestDTO) {
        TransactionResponseDTO response = transactionService.processTransaction(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}