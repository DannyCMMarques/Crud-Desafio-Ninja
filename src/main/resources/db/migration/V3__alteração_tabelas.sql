
CREATE TYPE especialidade_enum AS ENUM (
  'TAIJUTSU',
  'NINJUTSU',
  'GENJUTSU'
);

ALTER TABLE personagens
  DROP COLUMN tipo,
  ALTER COLUMN chakra SET DEFAULT 100,
  ADD COLUMN vida INT NOT NULL DEFAULT 5,
  ADD COLUMN especialidade especialidade_enum NOT NULL;


  ALTER TABLE jutsus
  ADD COLUMN dano INT NOT NULL,
  ADD COLUMN consumo_de_chakra INT NOT NULL,
  ADD COLUMN categoria especialidade_enum NOT NULL;
  


