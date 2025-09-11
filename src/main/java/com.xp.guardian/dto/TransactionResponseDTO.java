package com.xp.guardian.dto;

import com.xp.guardian.enums.TransactionStatus;
import java.math.BigDecimal;
import java.time.Instant;

public record TransactionResponseDTO(
        Long transactionId,
        Long clientId,
        BigDecimal amount,
        String description,
        TransactionStatus status,
        Instant timestamp,
        String message,
        InvestmentSuggestionDTO investmentSuggestion
) {}