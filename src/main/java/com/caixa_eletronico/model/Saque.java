package com.caixa_eletronico.model;

public class Saque extends Transacao {
    
    private double valor;

    public Saque(double valor, Conta conta, CaixaEletronico caixaEletronico) {
        // Chama o construtor da classe mãe (Transacao)
        super(conta, caixaEletronico); 
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }
}
