package com.xp.guardian.dto;

import com.xp.guardian.model.InvestmentSuggestion;
import java.time.Instant;

public record InvestmentSuggestionDTO(
        Long id,
        String text,
        Instant createdAt
) {
    public static InvestmentSuggestionDTO fromEntity(InvestmentSuggestion suggestion) {
        if (suggestion == null) {
            return null;
        }
        return new InvestmentSuggestionDTO(
                suggestion.getId(),
                suggestion.getText(),
                suggestion.getCreatedAt()
        );
    }
}