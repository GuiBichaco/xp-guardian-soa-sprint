package com.xp.guardian.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BettingHouseService {

    private final Set<String> keywords;

    public BettingHouseService(@Value("${app.betting-houses.keywords}") List<String> keywords) {
        this.keywords = new HashSet<>(keywords);
    }

    public boolean isBettingHouseTransaction(String description) {
        if (description == null || description.isBlank()) {
            return false;
        }
        String lowerCaseDescription = description.toLowerCase();
        return keywords.stream().anyMatch(lowerCaseDescription::contains);
    }
}
