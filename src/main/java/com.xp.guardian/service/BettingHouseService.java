package com.xp.guardian.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BettingHouseService {

    private final Set<String> normalizedKeywords;

    public BettingHouseService(@Value("${app.betting-houses.keywords}") List<String> keywords) {
        this.normalizedKeywords = keywords.stream()
                .map(this::normalize) // Aplica a normalização a cada palavra-chave
                .collect(Collectors.toSet());
    }

    public boolean isBettingHouseTransaction(String description) {
        if (description == null || description.isBlank()) {
            return false;
        }

        // Normaliza a descrição da transação no momento da verificação
        String normalizedDescription = normalize(description);

        // Compara a descrição normalizada com a lista de palavras-chave normalizadas
        return normalizedKeywords.stream().anyMatch(normalizedDescription::contains);
    }

    private String normalize(String input) {
        // Converte para minúsculas e remove tudo que não for letra ou número
        return input.toLowerCase().replaceAll("[^a-z0-9]", "");
    }
}