DELETE FROM salas;
DELETE FROM professores;

-- Inserir salas
INSERT INTO salas (nome, bloco) VALUES ('Sala 101', 'A');
INSERT INTO salas (nome, bloco) VALUES ('Sala 102', 'A');
INSERT INTO salas (nome, bloco) VALUES ('Sala 201', 'B');
INSERT INTO salas (nome, bloco) VALUES ('Laboratório 1', 'C');
INSERT INTO salas (nome, bloco) VALUES ('Laboratório 2', 'C');
INSERT INTO salas (nome, bloco) VALUES ('Auditório', 'D');

-- Inserir professores
INSERT INTO professores (nome, matricula, senha) VALUES ('João Silva', '12345', 'senha123');
INSERT INTO professores (nome, matricula, senha) VALUES ('Maria Santos', '67890', 'senha456');
INSERT INTO professores (nome, matricula, senha) VALUES ('Pedro Oliveira', '11111', 'senha789');
INSERT INTO professores (nome, matricula, senha) VALUES ('Ana Costa', '22222', 'senha000');
