package com.caixa_eletronico.model;

public class Deposito extends Transacao {

    private double valor;

    public Deposito(double valor, Conta conta, CaixaEletronico caixaEletronico) {
        super(conta, caixaEletronico);
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }
}