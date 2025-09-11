package com.xp.guardian.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record TransactionRequestDTO(
        @NotNull(message = "O ID do cliente não pode ser nulo.")
        Long clientId,

        @NotNull(message = "O valor não pode ser nulo.")
        @Positive(message = "O valor da transação deve ser positivo.")
        BigDecimal amount,

        @NotBlank(message = "A descrição não pode estar em branco.")
        String description
) {}