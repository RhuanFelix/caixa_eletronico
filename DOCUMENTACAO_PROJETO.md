# Documentação do Projeto: Caixa Eletrônico

Este documento detalha a arquitetura, o fluxo de funcionamento e as decisões de design do projeto de simulação de Caixa Eletrônico.

## 1. Visão Geral do Projeto

O projeto é uma aplicação de desktop que simula as operações de um caixa eletrônico (ATM). Ele possui uma interface gráfica que permite ao usuário se autenticar e realizar transações bancárias básicas.

### 1.1. Funcionalidades Principais
- **Autenticação de Usuário:** Login via número do cartão e PIN.
- **Operações Bancárias:**
    - Saque
    - Depósito
    - Transferência entre contas
    - Consulta de saldo
- **Persistência de Dados:** Todas as informações de clientes, contas e transações são salvas em um banco de dados.

### 1.2. Stack de Tecnologias
- **Linguagem:** Java (JDK 17)
- **Interface Gráfica (GUI):** JavaFX
- **Banco de Dados:** PostgreSQL
- **Gerenciador de Projeto e Dependências:** Maven

---

## 2. Arquitetura e Fluxo de Funcionamento

O sistema é modelado em camadas, separando a interface do usuário, da lógica de negócio e do acesso a dados.

### 2.1. Camada de Modelo (Pacote `model`)

Esta camada contém as classes que representam as entidades do mundo real (o "coração" da lógica de negócio).

- **`Cliente`**: Representa o correntista, com seus dados pessoais e status.
- **`Conta`**: Representa a conta bancária, ligada a um `Cliente`. Controla a lógica de `sacar()` e `depositar()`.
- **`Cartao`**: Representa o cartão físico, usado para a autenticação.
- **`Transacao`**: Classe abstrata que serve de base para todas as operações (`Saque`, `Deposito`, `Transferencia`, `ConsultaSaldo`).
- **`CaixaEletronico`**: A classe central que orquestra todo o fluxo. Ela contém a lógica para autenticar usuários e executar as transações, servindo como um "cérebro" para a aplicação.

### 2.2. Fluxo do Programa (Passo a Passo)

1.  **Inicialização (`App.java`)**: O programa inicia, carrega a tela de login (`LoginView.fxml`) e cria uma instância do `CaixaEletronico`, injetando nela as dependências de acesso ao banco de dados (os repositórios).
2.  **Autenticação (Login)**:
    - O usuário insere o número do cartão e o PIN.
    - O `LoginController` chama o método `autenticarUsuario()` da classe `CaixaEletronico`.
    - O `CaixaEletronico` usa o repositório de cartão para validar os dados com o banco de dados.
    - Se a autenticação for bem-sucedida, a sessão do usuário é iniciada e a tela principal (`MainView.fxml`) é exibida.
3.  **Execução de Operações (Tela Principal)**:
    - O usuário seleciona uma operação (ex: Saque).
    - O `MainController` chama o método correspondente no objeto `CaixaEletronico` (ex: `realizarSaque()`).
    - O `CaixaEletronico` executa a lógica de negócio (verifica saldo, etc.), atualiza o saldo no objeto `Conta` e, em seguida, usa os repositórios para persistir a mudança no banco de dados (atualizando o saldo da conta e salvando um registro da transação).
4.  **Encerramento da Sessão**: O usuário clica em "Sair", o que chama o método `encerrarSessao()` no `CaixaEletronico`, limpando os dados da sessão e retornando à tela de login.

---

## 3. Camada de Persistência (Repository Pattern)

O projeto utiliza o **Repository Pattern** para desacoplar a lógica de negócio da tecnologia de banco de dados. Isso é feito através dos pacotes `repository` e `persistence`.

### 3.1. Pacote `repository` (Interfaces / "Contratos")

Contém as `interfaces` Java que definem um "contrato" para acesso a dados. Elas ditam **quais** operações são possíveis, mas não se preocupam em **como** elas são implementadas.

- **Exemplo (`ContaRepository.java`)**: Define que deve existir um método `atualizarSaldo(Conta conta)`, mas não contém o código SQL para isso.

### 3.2. Pacote `persistence` (Implementações / "Trabalhadores")

Contém as classes concretas que `implementam` as interfaces do pacote `repository`. Elas contêm a lógica específica para o banco de dados PostgreSQL.

- **Exemplo (`ContaRepositoryPostgreSQL.java`)**: Implementa a interface `ContaRepository` e, dentro do método `atualizarSaldo()`, possui o código JDBC e a query SQL `UPDATE contas SET ...` para efetivamente alterar os dados no banco.

### 3.3. Vantagens desta Abordagem

- **Desacoplamento**: A lógica de negócio (`CaixaEletronico`) depende apenas das interfaces, não das implementações concretas. Ela não "sabe" que está usando um banco de dados PostgreSQL.
- **Flexibilidade e Manutenção**: Se o banco de dados for trocado no futuro (ex: para MySQL), basta criar novas classes de implementação no pacote `persistence`. Nenhuma alteração é necessária na lógica de negócio, tornando o sistema muito mais fácil de manter e adaptar.
- **Separação de Responsabilidades**: Mantém o código organizado. A lógica de negócio fica em um lugar, e o código de acesso ao banco de dados fica em outro.
