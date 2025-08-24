package com.caixa_eletronico.model;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConsultaSaldo extends Transacao {

    public ConsultaSaldo(Conta conta, CaixaEletronico caixaEletronico) {
        // Chama o construtor da classe-mãe 'Transacao'
        super(conta, caixaEletronico);
    }

    // Sobrescrevendo os metodos abstratos específicos para o tipo consultaSaldo
    @Override
    public String getTipoTransacao() {
        return "CONSULTA_SALDO";
    }

    @Override
    public void preencherParametrosEspecificos(PreparedStatement pstmt) throws SQLException {
        pstmt.setNull(4, java.sql.Types.DECIMAL);   //Setando null no placeholder 4
        pstmt.setNull(7, java.sql.Types.BIGINT);    //Setando null no placeholder 7
    }
}
