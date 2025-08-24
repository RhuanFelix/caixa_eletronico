package com.caixa_eletronico.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Deposito extends Transacao {

    private double valor;

    public Deposito(double valor, Conta conta, CaixaEletronico caixaEletronico) {
        super(conta, caixaEletronico);
        this.valor = valor;
    }

    // Sobrescrevendo os metodos abstratos específicos para o tipo deposito
    @Override
    public String getTipoTransacao() {
        return "DEPOSITO";
    }

    @Override
    public void preencherParametrosEspecificos(PreparedStatement pstmt) throws SQLException {
        pstmt.setDouble(4, this.getValor());
        pstmt.setNull(7, java.sql.Types.BIGINT);    //Setando null no placeholder 7
    }

    public double getValor() {
        return valor;
    }
}