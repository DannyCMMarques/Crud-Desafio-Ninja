--seed de personagens 

INSERT INTO personagens (nome, idade, aldeia, especialidade)
VALUES
  ('Naruto Uzumaki',    17,   'Konoha',     'NINJUTSU'),
  ('Sasuke Uchiha',     17,   'Konoha',     'NINJUTSU'),
  ('Sakura Haruno',     17,   'Konoha',     'TAIJUTSU'),
  ('Kakashi Hatake',    29,   'Konoha',     'NINJUTSU'),
  ('Might Guy',         29,   'Konoha',     'TAIJUTSU'),
  ('Rock Lee',          17,   'Konoha',     'TAIJUTSU'),
  ('Itachi Uchiha',     21,   'Konoha',     'NINJUTSU'),
  ('Shikamaru Nara',    17,   'Konoha',     'NINJUTSU'),
  ('Hinata Hyuga',      17,   'Konoha',     'TAIJUTSU'),
  ('Gaara',             17,   'Sunagakure', 'NINJUTSU'),
  ('Kurenai Yuhi',      29,   'Konoha',     'GENJUTSU'),
  ('Shisui Uchiha',     22,   'Konoha',     'GENJUTSU'),
  ('Madara Uchiha',     1000, 'Uchiha',     'GENJUTSU');

  -- Seed de jutsus 

INSERT INTO jutsus (tipo, categoria, dano, consumo_de_chakra) VALUES
  -- NINJUTSU
  ('Rasengan',           'NINJUTSU', 40, 20),
  ('Chidori',            'NINJUTSU', 50, 25),
  ('Clone das Sombras',  'NINJUTSU', 30, 15),
  ('Dragão de Água',     'NINJUTSU', 45, 18),
  ('Grande Bola de Fogo','NINJUTSU', 60, 24),
  ('Muralha de Lama',    'NINJUTSU', 35, 14),

  -- TAIJUTSU
  ('Entrada Dinâmica',   'TAIJUTSU', 35, 15),
  ('Lótus Primário',     'TAIJUTSU', 60, 30),
  ('Punho Gêmeo de Leão','TAIJUTSU', 55, 25),
  ('Folha Voadora',      'TAIJUTSU', 45, 18),
  ('Esquiva Relâmpago',  'TAIJUTSU', 30, 12),
  ('Barragem de Golpes', 'TAIJUTSU', 65, 26),

  -- GENJUTSU
  ('Tsukuyomi',          'GENJUTSU', 50, 25),
  ('Izanami',            'GENJUTSU', 45, 20),
  ('Mugen Tsukuyomi',    'GENJUTSU', 60, 30),
  ('Ilusão de Sombras',  'GENJUTSU', 40, 16),
  ('Hipnose Demoníaca',  'GENJUTSU', 35, 14),
  ('Rede Mental',        'GENJUTSU', 30, 12);
  

  INSERT INTO personagem_jutsus (personagem_id, jutsu_id)
SELECT p.id, j.id
FROM personagens p
JOIN jutsus j
  ON p.especialidade = j.categoria
ON CONFLICT (personagem_id, jutsu_id) DO NOTHING;