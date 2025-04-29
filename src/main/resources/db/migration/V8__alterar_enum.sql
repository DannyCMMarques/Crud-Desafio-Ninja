ALTER TABLE personagens
  ALTER COLUMN especialidade 
    TYPE VARCHAR(20) 
    USING especialidade::text;

ALTER TABLE jutsus
  ALTER COLUMN categoria 
    TYPE VARCHAR(20) 
    USING categoria::text;