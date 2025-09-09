CREATE TABLE client (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    balance DECIMAL(19, 2) NOT NULL
);

CREATE TABLE transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    amount DECIMAL(19, 2) NOT NULL,
    description VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    client_id BIGINT,
    FOREIGN KEY (client_id) REFERENCES client(id)
);

CREATE TABLE investment_suggestion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    text VARCHAR(512) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    client_id BIGINT,
    FOREIGN KEY (client_id) REFERENCES client(id)
);