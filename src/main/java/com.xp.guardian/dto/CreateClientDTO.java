package com.xp.guardian.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record CreateClientDTO(
        @NotBlank(message = "O nome não pode estar em branco.")
        String name,

        @NotBlank(message = "O email não pode estar em branco.")
        @Email(message = "O email deve ser válido.")
        String email,

        @NotNull(message = "O saldo inicial não pode ser nulo.")
        @PositiveOrZero(message = "O saldo inicial deve ser zero ou maior.")
        BigDecimal initialBalance
) {}