ALTER TABLE batalha
  ALTER COLUMN status 
  TYPE VARCHAR(20) 
  USING status::text;
