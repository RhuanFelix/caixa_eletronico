package com.caixa_eletronico.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Saque extends Transacao {
    
    private double valor;

    public Saque(double valor, Conta conta, CaixaEletronico caixaEletronico) {
        // Chama o construtor da classe mãe (Transacao)
        super(conta, caixaEletronico); 
        this.valor = valor;
    }

    // Sobrescrevendo os metodos abstratos específicos para o tipo saque
    @Override
    public String getTipoTransacao() {
        return "SAQUE";
    }

    @Override
    public void preencherParametrosEspecificos(PreparedStatement pstmt) throws SQLException {
        // Parâmetros do SQL: (4: valor, 7: conta_destino_id)
        pstmt.setDouble(4, this.getValor());
        pstmt.setNull(7, java.sql.Types.BIGINT);    //Setando null no placeholder 7
    }

    public double getValor() {
        return valor;
    }
}
