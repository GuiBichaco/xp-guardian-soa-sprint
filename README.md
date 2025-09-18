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
useCaseDiagram
    actor "Cliente da API" as User
    actor "Banco XP (Sistema Central)" as BankSystem

    rectangle "Sistema XP Guardian" {
        usecase "Cadastrar Novo Cliente" as UC_Create
        usecase "Processar Transação" as UC_Process
        usecase "Consultar Cliente" as UC_Consult
        usecase "Verificar se é Casa de Aposta" as UC_CheckBet
        usecase "Validar Saldo do Cliente" as UC_CheckBalance
        usecase "Bloquear Transação" as UC_Block
        usecase "Gerar Sugestão de Investimento" as UC_Suggest
    }

    User --> UC_Create
    User --> UC_Process
    User --> UC_Consult

    UC_Process ..> UC_CheckBet : <<include>>
    UC_Process ..> UC_CheckBalance : <<include>>
    
    UC_CheckBet --> UC_Block
    
    UC_Block ..> UC_Suggest : <<include>>

    UC_Suggest -- BankSystem : "consulta produtos"
```


