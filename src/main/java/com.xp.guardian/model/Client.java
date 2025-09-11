package com.xp.guardian.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
@Getter
@Setter
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private BigDecimal balance;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions = new ArrayList<>();

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<InvestmentSuggestion> investmentSuggestions = new ArrayList<>();

    public void setName(@NotBlank(message = "O nome não pode estar em branco.") String name) {
    }

    public void setEmail(@NotBlank(message = "O email não pode estar em branco.") @Email(message = "O email deve ser válido.") String email) {
    }

    public void setBalance(@NotNull(message = "O saldo inicial não pode ser nulo.") @PositiveOrZero(message = "O saldo inicial deve ser zero ou maior.") BigDecimal bigDecimal) {
    }
}