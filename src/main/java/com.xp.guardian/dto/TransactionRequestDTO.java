package com.xp.guardian.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequestDTO {

        @NotNull(message = "O ID do cliente não pode ser nulo.")
        private Long clientId;

        @NotNull(message = "O valor não pode ser nulo.")
        @Positive(message = "O valor da transação deve ser positivo.")
        private BigDecimal amount;

        @NotBlank(message = "A descrição não pode estar em branco.")
        private String description;
}