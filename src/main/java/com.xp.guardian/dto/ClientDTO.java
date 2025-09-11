package com.xp.guardian.dto;

import com.xp.guardian.model.Client;
import java.math.BigDecimal;

public record ClientDTO(
        Long id,
        String name,
        String email,
        BigDecimal balance
) {
    public static ClientDTO fromEntity(Client client) {
        return new ClientDTO(
                client.getId(),
                client.getName(),
                client.getEmail(),
                client.getBalance()
        );
    }
}