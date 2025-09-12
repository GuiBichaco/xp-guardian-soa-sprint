package com.xp.guardian.service;

import com.xp.guardian.dto.InvestmentSuggestionDTO;
import com.xp.guardian.dto.TransactionRequestDTO;
import com.xp.guardian.dto.TransactionResponseDTO;
import com.xp.guardian.enums.TransactionStatus;
import com.xp.guardian.exception.ClientNotFoundException;
import com.xp.guardian.model.Client;
import com.xp.guardian.model.InvestmentSuggestion;
import com.xp.guardian.model.Transaction;
import com.xp.guardian.repository.ClientRepository;
import com.xp.guardian.repository.InvestmentSuggestionRepository;
import com.xp.guardian.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final ClientRepository clientRepository;
    private final BettingHouseService bettingHouseService;
    private final InvestmentSuggestionRepository suggestionRepository;

    @Transactional
    public TransactionResponseDTO processTransaction(TransactionRequestDTO request) {
        Client client = clientRepository.findById(request.clientId())
                .orElseThrow(() -> new ClientNotFoundException("Cliente com ID " + request.clientId() + " não encontrado."));

        Transaction transaction = new Transaction();
        transaction.setClient(client);
        transaction.setAmount(request.amount());
        transaction.setDescription(request.description());
        transaction.setTimestamp(Instant.now());

        if (bettingHouseService.isBettingHouseTransaction(request.description())) {
            return blockTransactionAndSuggestInvestment(transaction, client);
        } else {
            return approveTransaction(transaction, client);
        }
    }

    private TransactionResponseDTO approveTransaction(Transaction transaction, Client client) {
        if (client.getBalance().compareTo(transaction.getAmount()) >= 0) {
            transaction.setStatus(TransactionStatus.APPROVED);
            client.setBalance(client.getBalance().subtract(transaction.getAmount()));
            clientRepository.save(client);
            transactionRepository.save(transaction);

            return new TransactionResponseDTO(
                    transaction.getId(), client.getId(), transaction.getAmount(), transaction.getDescription(),
                    transaction.getStatus(), transaction.getTimestamp(),
                    "Transação aprovada com sucesso.", null
            );
        } else {
            transaction.setStatus(TransactionStatus.FAILED);
            transactionRepository.save(transaction);

            return new TransactionResponseDTO(
                    transaction.getId(), client.getId(), transaction.getAmount(), transaction.getDescription(),
                    transaction.getStatus(), transaction.getTimestamp(),
                    "Transação falhou por saldo insuficiente.", null
            );
        }
    }

    private TransactionResponseDTO blockTransactionAndSuggestInvestment(Transaction transaction, Client client) {
        transaction.setStatus(TransactionStatus.BLOCKED);
        transactionRepository.save(transaction);

        InvestmentSuggestion suggestion = createSuggestion(client, transaction.getAmount());
        suggestionRepository.save(suggestion);

        client.getInvestmentSuggestions().add(suggestion);
        clientRepository.save(client);

        return new TransactionResponseDTO(
                transaction.getId(), client.getId(), transaction.getAmount(), transaction.getDescription(),
                transaction.getStatus(), transaction.getTimestamp(),
                "Transação bloqueada. Uma oportunidade de investimento foi gerada.",
                InvestmentSuggestionDTO.fromEntity(suggestion)
        );
    }

    private InvestmentSuggestion createSuggestion(Client client, BigDecimal amount) {
        String formattedAmount = String.format("%.2f", amount).replace('.', ',');
        String text = String.format(
                "Olá %s, notamos que você tentou gastar R$ %s em uma aposta. Que tal investir esse valor em nosso CDB com rendimento de 110%% do CDI?",
                client.getName().split(" ")[0], // Pega o primeiro nome
                formattedAmount
        );
        InvestmentSuggestion suggestion = new InvestmentSuggestion();
        suggestion.setClient(client);
        suggestion.setText(text);
        suggestion.setCreatedAt(Instant.now());
        return suggestion;
    }
}