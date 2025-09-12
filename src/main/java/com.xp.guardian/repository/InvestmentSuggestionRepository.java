package com.xp.guardian.repository;

import com.xp.guardian.model.InvestmentSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentSuggestionRepository extends JpaRepository<InvestmentSuggestion, Long> {
}