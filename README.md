```mermaid
useCaseDiagram
    actor "Cliente da API" as User

    rectangle "Serviços do XP Guardian" {
        usecase "Cadastrar Novo Cliente"
        usecase "Processar Transação Normal"
        usecase "Bloquear Transação de Aposta"
        usecase "Gerar Sugestão de Investimento"
        usecase "Consultar Dados do Cliente"
    }
    
    "Bloquear Transação de Aposta" ..> "Gerar Sugestão de Investimento" : << extends >>

    User --> "Cadastrar Novo Cliente"
    User --> "Processar Transação Normal"
    User --> "Bloquear Transação de Aposta"
    User --> "Consultar Dados do Cliente"
```
