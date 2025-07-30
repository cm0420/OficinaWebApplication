INSERT INTO id_sequences (entity_name, last_id) VALUES ('cliente', 0) ON CONFLICT (entity_name) DO NOTHING;
INSERT INTO id_sequences (entity_name, last_id) VALUES ('funcionario', 0) ON CONFLICT (entity_name) DO NOTHING;
INSERT INTO id_sequences (entity_name, last_id) VALUES ('carro', 0) ON CONFLICT (entity_name) DO NOTHING;
INSERT INTO id_sequences (entity_name, last_id) VALUES ('peca', 0) ON CONFLICT (entity_name) DO NOTHING;
INSERT INTO id_sequences (entity_name, last_id) VALUES ('ordem_de_servico', 0) ON CONFLICT (entity_name) DO NOTHING;

-- Cria um funcionário "Gerente" padrão para o primeiro login.
-- A senha é 'admin' criptografada com BCrypt.
-- IMPORTANTE: A sua aplicação DEVE usar BCryptPasswordEncoder para que isto funcione.
INSERT INTO funcionarios (id_usuario, cpf, nome, senha, cargo)
VALUES
    ('User-000', '12345678910', 'Admin Gerente', '1234', 'Gerente')
    ON CONFLICT (cpf) DO NOTHING;