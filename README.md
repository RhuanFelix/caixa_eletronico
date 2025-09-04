# Caixa Eletrônico - Simulação em Java

Este projeto é uma aplicação de desktop que simula as operações de um caixa eletrônico (ATM), desenvolvida em Java com uma interface gráfica utilizando JavaFX. A aplicação permite que usuários se autentiquem e realizem transações bancárias básicas, com todos os dados persistidos em um banco de dados PostgreSQL.

![Demonstração do Jogo Caça ao Tesouro](https://i.imgur.com/GLRXwXp.gif)


## Funcionalidades Principais

- **Autenticação de Usuário:** Login seguro via número do cartão e PIN.
- **Operações Bancárias:**
    - **Saque:** Retirada de valores da conta.
    - **Depósito:** Adição de valores à conta.
    - **Transferência:** Envio de valores para outra conta.
    - **Consulta de Saldo:** Verificação do saldo disponível.
- **Persistência de Dados:** Todas as informações de clientes, contas e transações são salvas em um banco de dados relacional.
- **Interface Gráfica:** Interface de usuário intuitiva construída com JavaFX e FXML.

## Stack de Tecnologias

- **Linguagem:** Java 17
- **Interface Gráfica (GUI):** JavaFX 21
- **Banco de Dados:** PostgreSQL
- **Gerenciador de Projeto e Dependências:** Maven

## Arquitetura do Projeto

O sistema é estruturado em camadas, seguindo os princípios do **Model-View-Controller (MVC)** e utilizando o **Repository Pattern** para o acesso a dados, garantindo um código desacoplado e de fácil manutenção.

- **Model (`com.caixa_eletronico.model`):** Contém as classes de negócio, como `Cliente`, `Conta`, `Cartao` e a classe orquestradora `CaixaEletronico`, que centraliza toda a lógica de negócio.
- **View (`resources/com/caixa_eletronico/view`):** Composta por arquivos FXML (`.fxml`) que definem a estrutura visual das telas e um arquivo CSS (`.css`) para estilização.
- **Controller (`com.caixa_eletronico.controller`):** Classes que manipulam a interação do usuário com a View, respondem a eventos (como cliques de botão) e se comunicam com o Model.
- **Repository (`com.caixa_eletronico.repository`):** Interfaces que definem o "contrato" de acesso aos dados (ex: `ContaRepository`). A lógica de negócio depende apenas dessas interfaces.
- **Persistence (`com.caixa_eletronico.persistence`):** Implementações concretas das interfaces do Repository, contendo a lógica específica de acesso ao banco de dados PostgreSQL com JDBC.

## Estrutura do Banco de Dados

O esquema do banco de dados foi projetado para suportar as funcionalidades da aplicação e inclui as seguintes tabelas principais:

- `clientes`
- `contas`
- `cartoes`
- `transacoes`
- `endereco`
- `telefone`
- `gerentes`
- `caixas_eletronicos`

O script SQL completo para criar e popular o banco de dados com dados de exemplo está no arquivo `SQL do CRUD.sql`.

## Como Executar o Projeto

Siga os passos abaixo para configurar e executar a aplicação em seu ambiente local.

### 1. Pré-requisitos

- **Java Development Kit (JDK):** Versão 17 ou superior.
- **Apache Maven:** Instalado e configurado no seu sistema.
- **PostgreSQL:** Um servidor de banco de dados PostgreSQL ativo.

### 2. Configuração do Banco de Dados

1.  Crie um novo banco de dados no seu servidor PostgreSQL com o nome `CaixaEletronico`.
2.  Execute o script `SQL do CRUD.sql` neste banco de dados para criar todas as tabelas e inserir os dados iniciais de teste.

### 3. Configuração da Conexão

As credenciais de acesso ao banco de dados estão na classe `ConexaoFactory.java`. Se necessário, ajuste os valores para corresponder à sua configuração local:

```java
// src/main/java/com/caixa_eletronico/connection/ConexaoFactory.java

private static final String URL = "jdbc:postgresql://localhost:5432/CaixaEletronico";
private static final String USUARIO = "postgres"; 
private static final String SENHA = "12345";
```

### 4. Execução

Abra um terminal na raiz do projeto e execute o seguinte comando Maven:

```bash
mvn clean javafx:run
```

O Maven irá compilar o projeto, baixar as dependências e iniciar a aplicação, abrindo a tela de login.

**Dados de Teste para Login:**
- **Cartão:** `123` / **PIN:** `123` (Cliente: Neymar)
- **Cartão:** `456` / **PIN:** `456` (Cliente: Messi)

## Estrutura de Diretórios

```
.
├── src
│   ├── main
│   │   ├── java/com/caixa_eletronico
│   │   │   ├── App.java                # Ponto de entrada da aplicação
│   │   │   ├── connection/             # Configuração da conexão JDBC
│   │   │   ├── controller/             # Controladores das telas
│   │   │   ├── model/                  # Classes de negócio
│   │   │   ├── persistence/            # Implementações do Repository (SQL)
│   │   │   └── repository/             # Interfaces do Repository
│   │   └── resources/com/caixa_eletronico
│   │       ├── view/                   # Arquivos FXML e CSS
├── pom.xml                             # Configuração do Maven
└── SQL do CRUD.sql                     # Script do banco de dados
```
