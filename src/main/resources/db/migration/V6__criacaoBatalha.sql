CREATE TYPE status_enum AS ENUM (
  'NAO_INICIADA',
  'EM_ANDAMENTO',
  'FINALIZADA'
);

CREATE TABLE batalha (
  id SERIAL PRIMARY KEY,
  criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  finalizado_em TIMESTAMP,
  status status_enum
);

CREATE TABLE participante_batalha (
  id SERIAL PRIMARY KEY,
  id_usuario INT NOT NULL
    REFERENCES usuarios(id) ON DELETE CASCADE,
  id_personagem INT NOT NULL
    REFERENCES personagens(id) ON DELETE CASCADE,
  id_batalha INT NOT NULL
    REFERENCES batalha(id) ON DELETE CASCADE,
  player_order INT NOT NULL, 
  vencedor BOOLEAN NOT NULL DEFAULT FALSE
);
