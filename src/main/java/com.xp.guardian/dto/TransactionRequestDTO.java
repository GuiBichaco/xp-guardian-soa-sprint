package com.xp.guardian.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionRequestDTO(
        @NotNull Long clientId,
        @NotNull @Positive BigDecimal amount,
        @NotBlank String description
) {}
