CREATE TABLE personagens (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    idade INT NOT NULL,
    aldeia VARCHAR(255) NOT NULL,
    chakra INT NOT NULL,
    tipo VARCHAR(255) NOT NULL,
    data_criacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE jutsus (
    id SERIAL PRIMARY KEY,
    tipo VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE personagem_jutsus (
    personagem_id INT NOT NULL,
    jutsu_id INT NOT NULL,
    PRIMARY KEY (personagem_id, jutsu_id),
    FOREIGN KEY (personagem_id) REFERENCES personagens(id) ON DELETE CASCADE,
    FOREIGN KEY (jutsu_id) REFERENCES jutsus(id) ON DELETE CASCADE
);
