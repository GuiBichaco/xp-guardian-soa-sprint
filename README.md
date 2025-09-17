graph TD
    subgraph "Cliente"
        A[👤 Usuário via Postman/Aplicação]
    end

    subgraph "Sistema XP Guardian (API REST)"
        B[Controller Layer<br>@RestController]
        C[Service Layer<br>@Service]
        D[Repository Layer<br>@Repository]
        E[Domain Models<br>@Entity]
    end

    subgraph "Banco de Dados"
        F[💾 H2 Database]
    end

    A -- Requisição HTTP (JSON) --> B
    B -- Chama Métodos de Serviço --> C
    C -- Usa Repositórios --> D
    D -- Mapeia Entidades --> E
    D -- Acessa Dados (JPA/Hibernate) --> F
