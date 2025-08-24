package com.caixa_eletronico.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Transferencia extends Transacao {

    private double valor;
    private Conta contaDestino; // apenas a conta de destino é específica da transferência

    public Transferencia(double valor, Conta contaOrigem, Conta contaDestino, CaixaEletronico caixaEletronico) {
        // a 'contaOrigem' é passada para o construtor da classe mãe 'Transacao' onde passa a ser a conta envolvida na operaçao
        super(contaOrigem, caixaEletronico); 
        this.valor = valor;
        this.contaDestino = contaDestino;
    }

    // Sobrescrevendo os metodos abstratos específicos para o tipo transferencia
    @Override
    public String getTipoTransacao() {
        return "TRANSFERENCIA";
    }

    @Override
    public void preencherParametrosEspecificos(PreparedStatement pstmt) throws SQLException {
        pstmt.setDouble(4, this.getValor());
        pstmt.setLong(7, this.getContaDestino().getId());
}

    // Getters para os atributos específicos
    public double getValor() {
        return valor;
    }

    public Conta getContaDestino() {
        return contaDestino;
    }
}