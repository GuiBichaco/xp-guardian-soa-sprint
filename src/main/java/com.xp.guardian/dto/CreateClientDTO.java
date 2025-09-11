package com.xp.guardian.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record CreateClientDTO(
        @NotBlank String name,
        @Email String email,
        @NotNull @PositiveOrZero BigDecimal initialBalance
) {}
