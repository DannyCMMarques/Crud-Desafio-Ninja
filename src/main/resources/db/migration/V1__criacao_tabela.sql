CREATE TABLE personagens(
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    idade INT NOT NULL,
    aldeia VARCHAR(255) NOT NULL,
    chackra INT NOT NULL,
    jutsus TEXT[] NOT NULL,
    tipo VARCHAR(255) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
