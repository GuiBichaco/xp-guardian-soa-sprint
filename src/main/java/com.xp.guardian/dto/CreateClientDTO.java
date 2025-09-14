package com.xp.guardian.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientDTO {

        @NotBlank(message = "O nome não pode estar em branco.")
        private String name;

        @NotBlank(message = "O email não pode estar em branco.")
        @Email(message = "O email deve ser válido.")
        private String email;

        @NotNull(message = "O saldo inicial não pode ser nulo.")
        @PositiveOrZero(message = "O saldo inicial deve ser zero ou maior.")
        private BigDecimal initialBalance;
}