package com.xp.guardian.dto;

import com.xp.guardian.model.Client;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public record ClientDetailsDTO(
        Long id,
        String name,
        String email,
        BigDecimal balance,
        List<InvestmentSuggestionDTO> investmentSuggestions
) {
    public static ClientDetailsDTO fromEntity(Client client) {
        return new ClientDetailsDTO(
                client.getId(),
                client.getName(),
                client.getEmail(),
                client.getBalance(),
                client.getInvestmentSuggestions().stream()
                        .map(InvestmentSuggestionDTO::fromEntity)
                        .collect(Collectors.toList())
        );
    }
}