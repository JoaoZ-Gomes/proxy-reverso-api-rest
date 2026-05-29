-- Rodar na VM 2 após instalar o PostgreSQL local.
-- Executar como: sudo -u postgres psql -f init.sql

-- Criar o banco de dados
CREATE DATABASE transacoes_db;

-- Conectar ao banco criado
\c transacoes_db

-- Criar a tabela
CREATE TABLE IF NOT EXISTS transacoes (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    valor NUMERIC(19, 2) NOT NULL,
    data DATE NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    CONSTRAINT chk_tipo CHECK (tipo IN ('RECEITA', 'DESPESA'))
);

-- Índices para consultas filtradas por data ou tipo
CREATE INDEX idx_transacoes_data ON transacoes(data);
CREATE INDEX idx_transacoes_tipo ON transacoes(tipo);

-- Dados de teste
INSERT INTO transacoes (descricao, valor, data, tipo) VALUES
('Salário', 3000.00, '2026-05-24', 'RECEITA'),
('Aluguel', 1500.00, '2026-05-20', 'DESPESA'),
('Supermercado', 250.50, '2026-05-23', 'DESPESA'),
('Freelance', 800.00, '2026-05-22', 'RECEITA'),
('Conta de Luz', 180.00, '2026-05-21', 'DESPESA');

-- Verificar dados
SELECT * FROM transacoes ORDER BY data DESC;
