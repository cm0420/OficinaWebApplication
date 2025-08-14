-- Cria tabela de sequência de IDs (se não existir)
CREATE TABLE IF NOT EXISTS id_sequences (
    entity_name VARCHAR(255) PRIMARY KEY,
    last_id INT NOT NULL
);

-- Criação da tabela funcionarios
CREATE TABLE IF NOT EXISTS funcionarios (
    id_usuario VARCHAR(50) PRIMARY KEY,
    cpf VARCHAR(11) UNIQUE NOT NULL,
    nome VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    cargo VARCHAR(50) NOT NULL,
    email VARCHAR(255)
);

-- Inicializa sequências para todas as entidades
INSERT INTO id_sequences (entity_name, last_id) VALUES ('cliente', 0) ON CONFLICT (entity_name) DO NOTHING;
INSERT INTO id_sequences (entity_name, last_id) VALUES ('funcionario', 0) ON CONFLICT (entity_name) DO NOTHING;
INSERT INTO id_sequences (entity_name, last_id) VALUES ('carro', 0) ON CONFLICT (entity_name) DO NOTHING;
INSERT INTO id_sequences (entity_name, last_id) VALUES ('peca', 0) ON CONFLICT (entity_name) DO NOTHING;
INSERT INTO id_sequences (entity_name, last_id) VALUES ('ordem_de_servico', 0) ON CONFLICT (entity_name) DO NOTHING;


-- Exemplo: adicione aqui a criação das demais tabelas necessárias
-- CREATE TABLE IF NOT EXISTS cliente (...);
-- CREATE TABLE IF NOT EXISTS carro (...);
-- CREATE TABLE IF NOT EXISTS peca (...);
-- CREATE TABLE IF NOT EXISTS ordem_de_servico (...);
