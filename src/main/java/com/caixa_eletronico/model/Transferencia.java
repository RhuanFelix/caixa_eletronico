package com.caixa_eletronico.model;

public class Transferencia extends Transacao {

    private double valor;
    private Conta contaDestino; // apenas a conta de destino é específica da transferência

    public Transferencia(double valor, Conta contaOrigem, Conta contaDestino, CaixaEletronico caixaEletronico) {
        // a 'contaOrigem' é passada para o construtor da classe mãe 'Transacao' onde passa a ser a conta envolvida na operaçao
        super(contaOrigem, caixaEletronico); 
        this.valor = valor;
        this.contaDestino = contaDestino;
    }

    // Getters para os atributos específicos
    public double getValor() {
        return valor;
    }

    public Conta getContaDestino() {
        return contaDestino;
    }
}