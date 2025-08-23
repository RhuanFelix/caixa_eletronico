package com.caixa_eletronico.model;

import com.caixa_eletronico.repository.*;

public class CaixaEletronico {

    private String id;
    private String endereco;
    private Cartao cartaoAtual;
    private Conta contaAtual;
    private Gerente gerenteResponsavel;
    private ContaRepository contaRepository;  // Dependencia do respository

    public CaixaEletronico(String id, String endereco, Gerente gerenteResponsavel, ContaRepository contaRepository) {
        this.id = id;
        this.endereco = endereco;
        this.cartaoAtual = null;
        this.contaAtual = null;
        this.gerenteResponsavel = gerenteResponsavel;
        this.contaRepository = contaRepository;   // Injetando a dependencia - evita acoplamento
    }


    public boolean autenticarUsuario(Cartao cartaoInserido, String pinDigitado) {
        if (cartaoInserido != null && cartaoInserido.isValido() && cartaoInserido.verificarPin(pinDigitado)) {
            this.cartaoAtual = cartaoInserido;
            this.contaAtual = cartaoInserido.getConta();
            System.out.println("Autenticação bem-sucedida. Bem-vindo, " + this.contaAtual.getTitular().getNome());
            return true;
        } else {
            System.out.println("Falha na autenticação. Cartão inválido, expirado ou PIN incorreto.");
            return false;
        }
    }

    //MÉTODOS DE OPERAÇÕES ---

    public void realizarSaque(double valor) {
        if (this.contaAtual == null) {
            System.out.println("Erro: Nenhuma sessão ativa.");
            return;
        }

        boolean sucesso = this.contaAtual.sacar(valor);     // atualiza o saldo na memoria

        if (sucesso) {
            this.contaRepository.atualizarSaldo(this.contaAtual);   // agora atualiza o saldo no banco

            this.dispensarDinheiro(valor);

            // CRIA O REGISTRO DA TRANSAÇÃO
            Saque transacao = new Saque(valor, this.contaAtual, this);
            transacao.setStatus("CONCLUIDA");
            
            // aqui iremos Chamar um TransacaoRepository para salvar o objeto 'transacao' no banco de dados.
            System.out.println("Transação de SAQUE registrada com sucesso.");

            this.imprimirRecibo("Saque", valor);
        }
    }


    public void realizarDeposito(double valor) {
        if (this.contaAtual == null) {
            System.out.println("Erro: Nenhuma sessão ativa.");
            return;
        }

        // Simula o caixa eletrônico recebendo o dinheiro antes de depositar na conta
        this.receberDinheiro(valor);

        this.contaAtual.depositar(valor);       // atualiza o saldo na memoria

        this.contaRepository.atualizarSaldo(this.contaAtual);   // agora atualiza o saldo no banco

        // CRIA O REGISTRO DA TRANSAÇÃO
        Deposito transacao = new Deposito(valor, this.contaAtual, this);
        transacao.setStatus("CONCLUIDA");

        // Aqui vamos Chamar um TransacaoRepository para salvar o objeto 'transacao' no banco de dados.
        System.out.println("Transação de DEPÓSITO registrada com sucesso.");
        
        this.imprimirRecibo("Depósito", valor);
    }

    //Orquestra a operação de transferência entre contas.

    public void realizarTransferencia(double valor, String numeroContaDestino) {
        if (this.contaAtual == null) {
            System.out.println("Erro: Nenhuma sessão ativa.");
            return;
        }

        // 1. Validações iniciais
        if (valor <= 0) {
            System.out.println("Erro: O valor da transferência deve ser positivo.");
            return;
        }
        if (this.contaAtual.getSaldoDisponivel() < valor) {
            System.out.println("Erro: Saldo insuficiente para realizar a transferência.");
            return;
        }
        
        // 2. Buscar a conta de destino usando o repository
        Conta contaDestino = this.contaRepository.buscarPorNumero(numeroContaDestino);

        // 3. Validar a conta de destino
        if (contaDestino == null) {
            System.out.println("Erro: A conta de destino não foi encontrada.");
            return;
        }
        if (this.contaAtual.getNumeroConta().equals(contaDestino.getNumeroConta())) {
            System.out.println("Erro: A conta de origem e destino não podem ser a mesma.");
            return;
        }

        // 4. Executar a transação
        boolean sucessoSaque = this.contaAtual.sacar(valor);
        
        if (sucessoSaque) {
            contaDestino.depositar(valor);

            // 5. Persistir AMBAS as mudanças no banco de dados
            this.contaRepository.atualizarSaldo(this.contaAtual);
            this.contaRepository.atualizarSaldo(contaDestino);

            // 6. CRIA O REGISTRO DA TRANSAÇÃO
            Transferencia transacao = new Transferencia(valor, this.contaAtual, contaDestino, this);
            transacao.setStatus("CONCLUIDA");

            // 7. aqui vamos Chamar um TransacaoDAO para salvar o objeto 'transacao' no banco de dados.
            System.out.println("Transação de TRANSFERÊNCIA registrada com sucesso.");

            this.imprimirRecibo("Transferência Enviada", valor);
        } else {
            // Se o saque falhou por alguma razão inesperada (já que o saldo foi pré-checado)
            System.out.println("Ocorreu um erro inesperado ao tentar realizar a transferência.");
        }
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
        ConsultaSaldo transacao = new ConsultaSaldo(this.contaAtual, this);
        transacao.setStatus("CONCLUIDA");

        // 3. aqui Chamar TransacaoRepository para salvar o objeto 'transacao' no banco de dados.
        System.out.println("Transação de CONSULTA DE SALDO registrada com sucesso.");
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
        System.out.println("Imprimindo recibo...");
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
}