package com.caixa_eletronico.model;

public class ConsultaSaldo extends Transacao {

    public ConsultaSaldo(Conta conta, CaixaEletronico caixaEletronico) {
        // Chama o construtor da classe-mãe 'Transacao'
        super(conta, caixaEletronico);
    }
}
