-- Estrutura da tabela "transacoes" no PostgreSQL.
-- O Hibernate cria automaticamente via ddl-auto=update, este script serve de referência.

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

SELECT * FROM transacoes ORDER BY data DESC;

-- Consultas de saldo
SELECT SUM(valor) as total_receitas FROM transacoes WHERE tipo = 'RECEITA';
SELECT SUM(valor) as total_despesas FROM transacoes WHERE tipo = 'DESPESA';

SELECT
    (SELECT SUM(valor) FROM transacoes WHERE tipo = 'RECEITA') as total_receitas,
    (SELECT SUM(valor) FROM transacoes WHERE tipo = 'DESPESA') as total_despesas,
    (SELECT SUM(valor) FROM transacoes WHERE tipo = 'RECEITA') -
    (SELECT SUM(valor) FROM transacoes WHERE tipo = 'DESPESA') as saldo;

-- DROP TABLE IF EXISTS transacoes;
