# Documentação da Arquitetura JavaFX

Este documento detalha o funcionamento da camada de interface gráfica (View) construída com JavaFX e como ela se comunica com os Controllers e a camada de negócio (Model) no projeto Caixa Eletrônico.

---

## 1. O Padrão de Comunicação em JavaFX (View-Controller)

A conexão entre a aparência da aplicação (View) e a lógica de interação (Controller) é feita através de um poderoso mecanismo de mapeamento fornecido pelo JavaFX.

- **View (Arquivos `.fxml`)**: São arquivos baseados em XML que definem a estrutura e o layout dos componentes visuais (botões, campos de texto, etc.). Eles são responsáveis exclusivamente pela **aparência**.

- **Controller (Classes `.java`)**: São classes Java que contêm a lógica para manipular os componentes da View, responder a interações do usuário (como cliques de botão) e orquestrar a comunicação com o Model.

A "mágica" da conexão acontece através de três elementos principais:

1.  **Atributo `fx:controller`**: Usado no elemento raiz do arquivo FXML para declarar qual classe Java é o seu controller.
    - *Exemplo*: `fx:controller="com.caixa_eletronico.controller.LoginController"`

2.  **Atributo `fx:id`**: Usado nos componentes dentro do FXML para dar-lhes um identificador único, como um nome de variável.
    - *Exemplo*: `<TextField fx:id="txtUsuario" ... />`

3.  **Anotação `@FXML`**: Usada nas variáveis dentro da classe Controller. O JavaFX **injeta** (atribui) automaticamente os componentes do FXML com um `fx:id` correspondente nas variáveis anotadas com `@FXML`.

4.  **Atributo `onAction`**: Usado em componentes interativos (como botões) para especificar o método no Controller que deve ser chamado quando o evento ocorre. O nome do método é precedido por `#`.
    - *Exemplo*: `<Button onAction="#handleLogin" ... />`

---

## 2. Análise da Tela de Login

A tela de login é o primeiro exemplo prático dessa comunicação.

- **View**: `src/main/resources/com/caixa_eletronico/view/LoginView.fxml`
- **Controller**: `src/main/java/com/caixa_eletronico/controller/LoginController.java`

### Fluxo de Execução:

1.  **Carregamento**: A aplicação inicia e carrega o `LoginView.fxml`.
2.  **Conexão**: O `fx:controller` no FXML diz ao JavaFX para criar uma instância de `LoginController`.
3.  **Injeção**: O JavaFX vê as variáveis `@FXML private TextField txtUsuario;` e `@FXML private PasswordField txtSenha;` no controller. Ele encontra os componentes com `fx:id="txtUsuario"` e `fx:id="txtSenha"` no FXML e os atribui a essas variáveis.
4.  **Interação (View -> Controller)**: O usuário clica no botão de Login. O atributo `onAction="#handleLogin"` no botão faz com que o método `handleLogin()` no `LoginController` seja chamado.
5.  **Lógica (Controller -> Model)**: Dentro de `handleLogin()`, o código pega os dados dos campos de texto (`txtUsuario.getText()`) e os envia para o Model para validação: `caixa.autenticarUsuario(numeroCartao, pin)`.
6.  **Atualização (Controller -> View)**: Com base no retorno `boolean` do Model, o Controller decide o que fazer:
    - Se `true`, carrega a `MainView.fxml`, efetivamente trocando de tela.
    - Se `false`, cria e exibe um `Alert` de erro para o usuário.

---

## 3. Análise da Tela Principal

A tela principal segue o mesmo padrão, mas com mais interações.

- **View**: `src/main/resources/com/caixa_eletronico/view/MainView.fxml`
- **Controller**: `src/main/java/com/caixa_eletronico/controller/MainController.java`

### Pontos Notáveis:

- **Inicialização de Dados**: O `MainController` possui um método customizado, `initData(CaixaEletronico caixa, Conta conta)`. Ele é chamado pelo `LoginController` após um login bem-sucedido para "passar" o Model (`caixa`) e a conta do usuário logado.
- **Atualização da View**: O método `atualizarInfoUsuario()` é chamado para preencher os `Labels` (`nomeLabel`, `saldoLabel`, etc.) com as informações da conta. Isso demonstra a comunicação **Controller -> View**.
- **Ciclo de Operação (Ex: Saque)**:
    1.  O botão "Saque" chama `handleSaque()`.
    2.  O Controller abre um diálogo para pegar o valor (cria uma "sub-view").
    3.  O Controller chama o Model: `caixa.realizarSaque(valor)`.
    4.  O Controller recebe o resultado e atualiza a interface:
        - Chama `atualizarInfoUsuario()` para exibir o novo saldo no `saldoLabel`.
        - Adiciona uma mensagem de log na `mensagemArea`.

---

## 4. O Ponto de Partida - `App.java`

A classe `App.java` é o ponto de entrada da aplicação e o orquestrador da inicialização.

- **Herança**: Ela estende `javafx.application.Application`, e a lógica principal está no método `start()`.
- **Criação do Model (Composition Root)**: O `start()` é responsável por criar todas as dependências da aplicação. Ele instancia as implementações concretas dos repositórios (`...PostgreSQL`) e as injeta no construtor do `CaixaEletronico`. Isso centraliza a configuração e desacopla o resto da aplicação do banco de dados específico.
- **Conexão com o Primeiro Controller**: Este é o passo mais importante. `App.java` usa `loader.setControllerFactory(...)` para interceptar a criação do `LoginController`. Dentro da "fábrica", ele:
    1.  Cria a instância do `LoginController`.
    2.  Chama o método `controller.setCaixa(caixa)`, injetando o Model principal no primeiro Controller.
    3.  Retorna o Controller configurado para o `FXMLLoader`.
- **Exibição**: Por fim, ele monta a `Scene`, a coloca no `Stage` (a janela principal) e a exibe com `stage.show()`.

Após o método `start()` ser concluído, o controle é totalmente entregue aos Controllers para gerenciar a interação com o usuário.
