---

### Mapeamento dos Casos de Uso para Serviços

A tabela a seguir representa de forma clara como cada caso de uso do sistema é implementado através de um serviço específico (endpoint da API) e qual componente da camada de serviço contém a lógica de negócio correspondente.

| Caso de Uso | Ator Principal | Endpoint da API (O Serviço) | Serviço Responsável (A Implementação) | Descrição |
| :--- | :--- | :--- | :--- | :--- |
| **Cadastrar Novo Cliente** | Cliente da API | `POST /api/v1/clients` | `ClientService.createClient()` | Cria um novo cliente no sistema com nome, email e saldo inicial. |
| **Processar Transação** | Cliente da API | `POST /api/v1/transactions` | `TransactionService.processTransaction()` | Ponto de entrada para qualquer transação. Orquestra a verificação para determinar se a transação é normal ou para uma casa de apostas. |
| **Bloquear Transação de Aposta** | (Sistema) | (Resultado do `POST /api/v1/transactions`) | `TransactionService.blockTransactionAndSuggestInvestment()` | Resultado do caso de uso "Processar Transação". O status da transação é definido como `BLOCKED` e ela é salva sem debitar o saldo do cliente. |
| **Gerar Sugestão de Investimento** | (Sistema) | (Resultado do `POST /api/v1/transactions`) | `TransactionService.createSuggestion()` | Consequência do bloqueio de uma transação. Uma nova entidade `InvestmentSuggestion` é criada e associada ao cliente. |
| **Consultar Dados do Cliente** | Cliente da API | `GET /api/v1/clients/{id}` | `ClientService.findClientById()` | Retorna os detalhes de um cliente específico, incluindo seu saldo atualizado e a lista de sugestões de investimento recebidas. |

---

Diagrama de Arquitetura

```mermaid
graph TD
    subgraph "Cliente"
        A[Postman]
    end

    subgraph "Aplicação XP Guardian"
        B["ClientController & TransactionController<br>(Camada de Controle)"]
        C["ClientService, TransactionService<br>& BettingHouseService<br>(Camada de Serviço)"]
        D["Repositories<br>(Camada de Acesso a Dados)"]
    end

    subgraph "Banco de Dados"
        E[H2 Database]
    end

    A -- "Requisição HTTP" --> B
    B -- "Chama" --> C
    C -- "Usa" --> D
    D -- "Acessa" --> E
```

---

 Diagrama de Entidades

 ```mermaid
erDiagram
    CLIENT {
        LONG id PK
        VARCHAR name
        VARCHAR email
        DECIMAL balance
    }

    TRANSACTION {
        LONG id PK
        DECIMAL amount
        VARCHAR description
        VARCHAR status
        TIMESTAMP timestamp
        LONG client_id FK
    }

    INVESTMENT_SUGGESTION {
        LONG id PK
        VARCHAR text
        TIMESTAMP created_at
        LONG client_id FK
    }

    CLIENT ||--o{ TRANSACTION : "realiza"
    CLIENT ||--o{ INVESTMENT_SUGGESTION : "recebe"
```

---

Diagrama de Casos de Uso

```mermaid
graph TD
    User(["👤<br>Cliente da API"])
    BankSystem(["🏦<br>Banco XP (Sistema Central)"])
    
    subgraph Sistema XP Guardian
        UC_CreateClient("Cadastrar Novo Cliente")
        UC_Process("Processar Transação")
        UC_GetClient("Consultar Cliente")

        UC_CheckBet("Verificar se é Casa de Aposta")
        UC_CheckBalance("Validar Saldo do Cliente")
        UC_Block("Bloquear Transação")
        UC_Suggest("Gerar Sugestão de Investimento")
    end

    User --> UC_CreateClient
    User --> UC_Process
    User --> UC_GetClient

    UC_Process -.->|inclui| UC_CheckBet
    UC_Process -.->|inclui| UC_CheckBalance
    
    UC_CheckBet -- "Se for aposta" --> UC_Block
    UC_Block -.->|inclui| UC_Suggest

    UC_Suggest ---|consulta produtos| BankSystem

    classDef default fill:#fff,stroke:#333,stroke-width:2px;
```


