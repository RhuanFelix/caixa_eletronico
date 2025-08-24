package com.caixa_eletronico.persistence;

import com.caixa_eletronico.model.Transacao;
import com.caixa_eletronico.repository.TransacaoRepository;
import java.sql.*;

public class TransacaoRepositoryPostgreSQL implements TransacaoRepository {

    @Override
    public void salvar(Transacao transacao) {
        String sql = "INSERT INTO transacoes (tipo_transacao, data_criacao, status, valor, " +
                     "conta_id, caixa_eletronico_id, conta_destino_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            
            // --- Parâmetros Comuns a todos os tipos de transacao ---
            pstmt.setTimestamp(2, Timestamp.valueOf(transacao.getDataCriacao()));
            pstmt.setString(3, transacao.getStatus());
            pstmt.setLong(5, transacao.getConta().getId());
            pstmt.setString(6, transacao.getCaixaEletronico().getId());

            // 1. Pede para a transação dizer qual o seu tipo (mantem polimorfismo).
            pstmt.setString(1, transacao.getTipoTransacao()); 
            
            // 2. Pede para a própria transação preencher os parâmetros que são específicos dela.
            transacao.preencherParametrosEspecificos(pstmt);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
