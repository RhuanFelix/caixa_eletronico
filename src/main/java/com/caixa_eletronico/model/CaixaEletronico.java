package com.caixa_eletronico.model;

import com.caixa_eletronico.repository.*;

public class CaixaEletronico {

    private String id;
    private String endereco;
    private Cartao cartaoAtual;
    private Conta contaAtual;
    private Gerente gerenteResponsavel;
    private ContaRepository contaRepository;  // Dependencia do respository de conta
    private CartaoRepository cartaoRepository;  // Dependencia do respository de cartao
    private TransacaoRepository transacaoRepository;    // Dependencia do respository de transacao
    

    public CaixaEletronico(String id, String endereco, Gerente gerenteResponsavel, ContaRepository contaRepository, CartaoRepository cartaoRepository, TransacaoRepository transacaoRepository) {
        this.id = id;
        this.endereco = endereco;
        this.cartaoAtual = null;
        this.contaAtual = null;
        this.gerenteResponsavel = gerenteResponsavel;
        this.contaRepository = contaRepository;   // Inversao da dependencia
        this.cartaoRepository = cartaoRepository;
        this.transacaoRepository = transacaoRepository;
    }


    public boolean autenticarUsuario(String numeroDoCartao, String pinDigitado) {
        // 1. Usa o repository para buscar o cartão no banco de dados.
        Cartao cartaoDoBanco = this.cartaoRepository.buscarPorNumero(numeroDoCartao);

        // 2. Primeira validação: o cartão existe?
        if (cartaoDoBanco == null) {
            System.out.println("Falha na autenticação. Cartão não encontrado.");
            return false;
        }

        // 3. Se o cartão existe, agora fazemos as validações no objeto que veio do banco.
        if (cartaoDoBanco.isValido() && cartaoDoBanco.verificarPin(pinDigitado)) {
            // Autenticação OK! Preenchemos os dados da sessão.
            this.cartaoAtual = cartaoDoBanco;
            this.contaAtual = cartaoDoBanco.getConta();
            System.out.println("Autenticação bem-sucedida. Bem-vindo, " + this.contaAtual.getTitular().getNome() + "!");
            return true;
        } else {
            // Se a validação do PIN ou da data de validade falhou.
            System.out.println("Falha na autenticação. PIN incorreto ou cartão expirado.");
            this.cartaoAtual = null; // Garante que a sessão não inicie
            this.contaAtual = null;
            return false;
        }
    }

    //MÉTODOS DE OPERAÇÕES ---

    public boolean realizarSaque(double valor) {
        if (this.contaAtual == null) {
            System.out.println("Erro: Nenhuma sessão ativa.");
            return false;
        }

        // VERIFICA SE O CLIENTE PODE REALIZAR A OPERAÇÃO
        if (!this.contaAtual.getTitular().podeRealizarTransacao()) {
            System.out.println("Operação negada. Status do cliente não permite transações.");
            return false;
        }

        boolean sucesso = this.contaAtual.sacar(valor);     // atualiza o saldo na memoria

        if (sucesso) {
            this.contaRepository.atualizarSaldo(this.contaAtual);   // agora atualiza o saldo no banco

            this.dispensarDinheiro(valor);

            // CRIA O REGISTRO DA TRANSAÇÃO
            Transacao transacao = new Saque(valor, this.contaAtual, this);
            
            // faz a persistencia da transacao no banco
            this.transacaoRepository.salvar(transacao);     
            System.out.println("\nTransação de SAQUE registrada com sucesso.");

            this.imprimirRecibo("Saque", valor);

            return true;
        }

        return false;
    }

    public void realizarDeposito(double valor) {
        if (this.contaAtual == null) {
            System.out.println("Erro: Nenhuma sessão ativa.");
            return;
        }

        // VERIFICA SE O CLIENTE PODE REALIZAR A OPERAÇÃO
        if (!this.contaAtual.getTitular().podeRealizarTransacao()) {
            System.out.println("Operação negada. Status do cliente não permite transações.");
            return;
        }

        // Simula o caixa eletrônico recebendo o dinheiro antes de depositar na conta
        this.receberDinheiro(valor);

        this.contaAtual.depositar(valor);       // atualiza o saldo na memoria

        this.contaRepository.atualizarSaldo(this.contaAtual);   // agora atualiza o saldo no banco

        // CRIA O REGISTRO DA TRANSAÇÃO
        Transacao transacao = new Deposito(valor, this.contaAtual, this);

        // faz a persistencia da transacao no banco
        this.transacaoRepository.salvar(transacao);     
        System.out.println("Transação de DEPÓSITO registrada com sucesso.");
        
        this.imprimirRecibo("Depósito", valor);
    }

    //Orquestra a operação de transferência entre contas.

    public boolean realizarTransferencia(double valor, String numeroContaDestino) {
        if (this.contaAtual == null) {
            System.out.println("Erro: Nenhuma sessão ativa.");
            return false;
        }

        // VERIFICA SE O CLIENTE PODE REALIZAR A OPERAÇÃO
        if (!this.contaAtual.getTitular().podeRealizarTransacao()) {
            System.out.println("Operação negada. Status do cliente não permite transações.");
            return false;
        }

        // 1. Validações iniciais
        if (valor <= 0) {
            System.out.println("Erro: O valor da transferência deve ser positivo.");
            return false;
        }
        if (this.contaAtual.getSaldoDisponivel() < valor) {
            System.out.println("Erro: Saldo insuficiente para realizar a transferência.");
            return false;
        }
        
        // 2. Buscar a conta de destino usando o repository
        Conta contaDestino = this.contaRepository.buscarPorNumero(numeroContaDestino);

        // 3. Validar a conta de destino
        if (contaDestino == null) {
            System.out.println("Erro: A conta de destino não foi encontrada.");
            return false;
        }
        if (this.contaAtual.getNumeroConta().equals(contaDestino.getNumeroConta())) {
            System.out.println("Erro: A conta de origem e destino não podem ser a mesma.");
            return false;
        }

        // 4. Executar a transação
        boolean sucessoSaque = this.contaAtual.sacar(valor);
        
        if (sucessoSaque) {
            contaDestino.depositar(valor);

            // 5. Persistir AMBAS as mudanças no banco de dados
            this.contaRepository.atualizarSaldo(this.contaAtual);
            this.contaRepository.atualizarSaldo(contaDestino);

            // 6. CRIA O REGISTRO DA TRANSAÇÃO
            Transacao transacao = new Transferencia(valor, this.contaAtual, contaDestino, this);

            // 7. faz a persistencia da transacao no banco
            this.transacaoRepository.salvar(transacao);     
            System.out.println("Transação de TRANSFERÊNCIA registrada com sucesso.");

            this.imprimirRecibo("Transferência Enviada", valor);
        } else {
            // Se o saque falhou por alguma razão inesperada (já que o saldo foi pré-checado)
            System.out.println("Ocorreu um erro inesperado ao tentar realizar a transferência.");
        }
        return true;
    }
    
    public void realizarConsultaSaldo() {
        if (this.contaAtual == null) {
            System.out.println("Erro: Nenhuma sessão ativa.");
            return;
        }

        // 1. Acessa a informação na conta
        double saldo = this.contaAtual.getSaldoDisponivel();
        System.out.printf("Seu saldo disponível é: R$ %.2f", saldo);

        // 2. CRIA O REGISTRO DA TRANSAÇÃO
        Transacao transacao = new ConsultaSaldo(this.contaAtual, this);

        // faz a persistencia da transacao no banco
        this.transacaoRepository.salvar(transacao);
        System.out.println("\nCONSULTA DE SALDO registrada com sucesso.");
    }

    public void encerrarSessao() {
        this.cartaoAtual = null;
        this.contaAtual = null;
        System.out.println("Sessão encerrada. Retire seu cartão.");
    }

    // MÉTODOS PRIVADOS (SIMULAÇÃO DE HARDWARE) ---
    
    private void receberDinheiro(double valor) {
        System.out.println("--- Simulação de recebimento do valor ---");
        System.out.println("Aguardando inserção das notas... R$ " + valor + " recebido e validado.");
        System.out.println("---------------------------");
    }

    private void dispensarDinheiro(double valor) {
        System.out.println("--- Simulação de saque ---");
        System.out.println("Dispensando R$ " + valor + " em notas.");
        System.out.println("---------------------------");
    }

    private void imprimirRecibo(String tipoOperacao, double valor) {
        System.out.println("\nImprimindo recibo...");
        System.out.println("==========================");
        System.out.println("      RECIBO CAIXA       ");
        System.out.println("Data: " + java.time.LocalDate.now());
        System.out.println("Operação: " + tipoOperacao);
        System.out.println("Valor: R$ " + valor);
        System.out.println("Conta: " + this.contaAtual.getNumeroConta());
        System.out.println("==========================");
        System.out.println("---------------------------");
    }

    // GETTERS
    public String getId() { return id; }
    public Conta getContaAtual() { return contaAtual; }
    public CartaoRepository getCartaoRepository() {
        return cartaoRepository;
    }
}
