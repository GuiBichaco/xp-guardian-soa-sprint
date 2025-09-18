# XP Guardian - API de Controle de Apostas e Sugestão de Investimentos

![Java](https://img.shields.io/badge/Java-21-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)
![Maven](https://img.shields.io/badge/Build-Maven-purple.svg)
![H2 Database](https://img.shields.io/badge/Database-H2-red.svg)

---

## 📝 Descrição do Projeto

O **XP Guardian** é um serviço RESTful desenvolvido em Java 21 e Spring Boot 3, que simula uma funcionalidade para o Banco XP. A API tem o objetivo de identificar e bloquear transações financeiras destinadas a casas de apostas e, proativamente, sugerir ao cliente a aplicação do valor economizado em produtos de investimento oferecidos pelo banco.

Este projeto foi desenvolvido para demonstrar boas práticas de arquitetura de software, código limpo, e o uso de tecnologias modernas no ecossistema Java.

## ✨ Features Principais

-   **Processamento de Transações**: Endpoint para receber e avaliar transações.
-   **Bloqueio Inteligente**: Identifica transações para casas de apostas (configurável) e as bloqueia.
-   **Sugestão de Investimentos**: Gera uma sugestão de investimento personalizada quando uma transação de aposta é bloqueada.
-   **Consulta de Clientes**: Permite consultar dados de clientes e suas sugestões de investimento.
-   **Tratamento de Erros**: Respostas de erro padronizadas e claras.

## 🚀 Tecnologias Utilizadas

-   **Linguagem**: Java 21
-   **Framework**: Spring Boot 3.x
-   **Banco de Dados**: H2 (In-Memory)
-   **Persistência**: Spring Data JPA / Hibernate
-   **Migrações**: Flyway
-   **Build Tool**: Maven
-   **Validação**: Jakarta Bean Validation
-   **Utilitários**: Lombok
-   **Testes da API**: Postman / cURL

## 🏛️ Arquitetura

A aplicação segue uma arquitetura em camadas bem definida para garantir a separação de responsabilidades, manutenibilidade e escalabilidade.

-   **Controller Layer**: Responsável por expor os endpoints REST, receber as requisições HTTP, validar os dados de entrada (DTOs) e delegar a lógica de negócio para a camada de serviço.
-   **Service Layer**: Contém a lógica de negócio principal da aplicação. Orquestra as operações entre os repositórios e outros serviços.
-   **Repository Layer**: Camada de acesso a dados, utilizando Spring Data JPA para abstrair a comunicação com o banco de dados.
-   **Domain/Model**: Contém as entidades JPA que representam as tabelas do banco de dados.
-   **DTOs (Data Transfer Objects)**: Objetos para transferir dados entre as camadas, especialmente entre a Controller e a Service, evitando a exposição das entidades de domínio.

## 📊 Diagramas do Projeto

### Arquitetura de Camadas
```mermaid
graph TD
    subgraph "Cliente"
        A[Postman / cURL]
    end

    subgraph "Aplicação XP Guardian"
        B["Controller Layer"]
        C["Service Layer"]
        D["Repository Layer"]
    end

    subgraph "Banco de Dados"
        E[H2 Database]
    end

    A -- "Requisição HTTP" --> B
    B -- "Chama" --> C
    C -- "Usa" --> D
    D -- "Acessa" --> E
```

### Diagrama de Entidades (ER Diagram)
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
    CLIENT ||--o{ TRANSACTION : realiza
    CLIENT ||--o{ INVESTMENT_SUGGESTION : recebe
```

### Diagrama de Casos de Uso
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

## 🔧 Configuração e Execução

### Pré-requisitos
* JDK 21 ou superior instalado.
* Apache Maven 3.8+ instalado.
* Uma IDE como IntelliJ IDEA ou VS Code.

### ❗️ Configuração Essencial da IDE (Lombok)
Este projeto utiliza **Lombok**. Para que sua IDE entenda o código e não mostre falsos erros, a configuração correta é fundamental.
1.  **Instale o Plugin do Lombok** na sua IDE.
2.  **Habilite o "Annotation Processing"** nas configurações da IDE (`Settings > Build, Execution, Deployment > Compiler > Annotation Processors`).
3.  Reinicie a IDE.

### Passos para Executar
1.  **Clone o repositório:**
    ```bash
    cd Desktop (se desejado)
    https://github.com/GuiBichaco/xp-guardian-soa-sprint.git
    cd xp-guardian-soa-sprint
    ```

2.  **Compile e execute o projeto com Maven:**
    ```bash
    mvn spring-boot:run
    ```

3.  A aplicação estará disponível em `http://localhost:8080`. O console do banco H2 pode ser acessado em `http://localhost:8080/h2-console`.

## 🎮 Exemplos de Requisições e Respostas (cURL)

### 1. Criar um novo Cliente

* **Request:**
    ```bash
    curl -X POST http://localhost:8080/api/v1/clients \
    -H "Content-Type: application/json" \
    -d '{
      "name": "João da Silva",
      "email": "joao.silva@email.com",
      "initialBalance": 1000.00
    }'
    ```
* **Response (`201 Created`):**
    ```json
    {
      "id": 1,
      "name": "João da Silva",
      "email": "joao.silva@email.com",
      "balance": 1000.00
    }
    ```

### 2. Processar uma Transação (Não-Aposta - Aprovada)

* **Request:**
    ```bash
    curl -X POST http://localhost:8080/api/v1/transactions \
    -H "Content-Type: application/json" \
    -d '{
      "clientId": 1,
      "amount": 75.50,
      "description": "Pagamento iFood"
    }'
    ```
* **Response (`201 Created`):**
    ```json
    {
        "transactionId": 1,
        "clientId": 1,
        "amount": 75.50,
        "description": "Pagamento iFood",
        "status": "APPROVED",
        "timestamp": "...",
        "message": "Transação aprovada com sucesso.",
        "investmentSuggestion": null
    }
    ```

### 3. Processar uma Transação (Casa de Aposta - Bloqueada)

* **Request:**
    ```bash
    curl -X POST http://localhost:8080/api/v1/transactions \
    -H "Content-Type: application/json" \
    -d '{
      "clientId": 1,
      "amount": 100.00,
      "description": "Depósito Bet365"
    }'
    ```
* **Response (`201 Created`):**
    ```json
    {
        "transactionId": 2,
        "clientId": 1,
        "amount": 100.00,
        "description": "Depósito Bet365",
        "status": "BLOCKED",
        "timestamp": "...",
        "message": "Transação bloqueada. Uma oportunidade de investimento foi gerada.",
        "investmentSuggestion": {
            "id": 1,
            "text": "Olá João, notamos que você tentou gastar R$ 100,00 em uma aposta. Que tal investir esse valor em nosso CDB com rendimento de 110% do CDI?",
            "createdAt": "..."
        }
    }
    ```

### 4. Consultar um Cliente e suas Sugestões

* **Request:**
    ```bash
    curl -X GET http://localhost:8080/api/v1/clients/1
    ```
* **Response (`200 OK`):**
    ```json
    {
        "id": 1,
        "name": "João da Silva",
        "email": "joao.silva@email.com",
        "balance": 924.50,
        "investmentSuggestions": [
            {
                "id": 1,
                "text": "Olá João, notamos que você tentou gastar R$ 100,00 em uma aposta. Que tal investir esse valor em nosso CDB com rendimento de 110% do CDI?",
                "createdAt": "..."
            }
        ]
    }
    ```

### 5. Tentar uma transação para um cliente inexistente

* **Request:**
    ```bash
    curl -X POST http://localhost:8080/api/v1/transactions \
    -H "Content-Type: application/json" \
    -d '{
      "clientId": 999,
      "amount": 50.00,
      "description": "Teste"
    }'
    ```
* **Response (`404 Not Found`):**
    ```json
    {
        "timestamp": "...",
        "status": 404,
        "error": "Recurso não encontrado",
        "message": "Cliente com ID 999 não encontrado.",
        "path": "/api/v1/transactions"
    }
    ```