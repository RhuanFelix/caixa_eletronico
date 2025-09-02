DROP TABLE IF EXISTS transacoes CASCADE;
DROP TABLE IF EXISTS cartoes CASCADE;
DROP TABLE IF EXISTS contas CASCADE;
DROP TABLE IF EXISTS caixas_eletronicos CASCADE;
DROP TABLE IF EXISTS gerentes CASCADE;
DROP TABLE IF EXISTS endereco CASCADE;
DROP TABLE IF EXISTS telefone CASCADE;
DROP TABLE IF EXISTS clientes CASCADE;

CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    status VARCHAR(20) NOT NULL -- (Valores: 'ATIVO', 'INATIVO', 'BLOQUEADO')
);

CREATE TABLE telefone(
	id SERIAL PRIMARY KEY,
	ddd CHAR(2) NOT NULL,
	numero VARCHAR(9) NOT NULL,
	id_cliente INT,
	FOREIGN KEY (id_cliente) REFERENCES clientes(id)
);

CREATE TABLE endereco(
	id SERIAL PRIMARY KEY,
	rua VARCHAR(150) NOT NULL,
	bairro VARCHAR(150) NOT NULL,
	numero_residencia INT NOT NULL,
	cidade VARCHAR(100) NOT NULL,
	uf CHAR(2) NOT NULL,
	id_cliente INT,
	FOREIGN KEY (id_cliente) REFERENCES clientes(id)
);

CREATE TABLE gerentes (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    matricula VARCHAR(50) NOT NULL UNIQUE
);


CREATE TABLE caixas_eletronicos (
    id VARCHAR(50) PRIMARY KEY, 
    endereco TEXT NOT NULL,
    gerente_id INT NOT NULL,
    FOREIGN KEY (gerente_id) REFERENCES gerentes (id)
);

CREATE TABLE contas (
    id SERIAL PRIMARY KEY,
    numero_conta VARCHAR(20) NOT NULL UNIQUE,
    saldo_total DECIMAL(12, 2) NOT NULL,
    saldo_disponivel DECIMAL(12, 2) NOT NULL,
    titular_id INT NOT NULL,
    FOREIGN KEY (titular_id) REFERENCES clientes(id)
);

CREATE TABLE cartoes (
    id SERIAL PRIMARY KEY,
    numero VARCHAR(16) NOT NULL UNIQUE,
    nome_impresso VARCHAR(255) NOT NULL,
    data_expiracao DATE NOT NULL,
    pin VARCHAR(255) NOT NULL, 
    conta_id INT NOT NULL UNIQUE, 
    FOREIGN KEY (conta_id) REFERENCES contas(id)
);

CREATE TABLE transacoes (
    id SERIAL PRIMARY KEY,
    tipo_transacao VARCHAR(20) NOT NULL, -- 'SAQUE', 'DEPOSITO', etc.
    data_criacao TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL,
    valor DECIMAL(12, 2), -- Nulo para Consulta de Saldo

    -- Chaves Estrangeiras
    conta_id INT NOT NULL,
    caixa_eletronico_id VARCHAR(50) NOT NULL,
    conta_destino_id INT, -- Apenas para Transferência, pode ser nulo

    FOREIGN KEY (conta_id) REFERENCES contas(id),
    FOREIGN KEY (caixa_eletronico_id) REFERENCES caixas_eletronicos(id),
    FOREIGN KEY (conta_destino_id) REFERENCES contas(id)
);

-- INSERINDO 

-- 1. inserindo gerente
INSERT INTO gerentes(nome, matricula) 
VALUES ('Jurandir', '123456789');

-- 2. inserindo CAIXA ELETRONICA(gerente precisa ser inserido antes, caixa eletronico depedende de gerente)
INSERT INTO caixas_eletronicos (id, endereco, gerente_id) 
VALUES ('ATM-001', 'Rua Principal, 123', 1);

-- 3. inserindo cliente
INSERT INTO clientes (nome, email, status)
VALUES ('Neymar', 'ney@email.com', 'ATIVO'),
	   ('Messi', 'messi@gmail.com', 'ATIVO');

-- 4. Inserir uma conta para o cliente
INSERT INTO contas (numero_conta, saldo_total, saldo_disponivel, titular_id)
VALUES ('111', 1000.00, 1000.00, 1),
	   ('222', 1000.00, 1000.00, 2);

-- 5. Inserir um cartão vinculado à conta criada
-- Primeiro precisamos saber o id da conta que acabou de criar. Supondo que seja 1
INSERT INTO cartoes (numero, nome_impresso, data_expiracao, pin, conta_id)
VALUES ('123', 'NEYMAR', '2027-12-31', '123', 1),
       ('456', 'MESSI', '2028-12-31', '456', 2);

-- 6. inserindo endereço
INSERT INTO endereco (rua, bairro, numero_residencia, cidade, uf, id_cliente)
VALUES ('Rua A', 'Bairro B', 123, 'Cidade C', 'PB', 1),
       ('Rua 1', 'Bairro 2', 456, 'Cidade 3', 'PE', 2);
-- 7. inserindo telefones
INSERT INTO telefone (ddd, numero, id_cliente) 
VALUES ('11', '999988887', 1), 
	   ('21', '988877766', 2);