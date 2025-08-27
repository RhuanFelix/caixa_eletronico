package com.caixa_eletronico.persistence;

import com.caixa_eletronico.model.*;
import com.caixa_eletronico.repository.CartaoRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.caixa_eletronico.connection.ConexaoFactory;

public class CartaoRepositoryPostgreSQL implements CartaoRepository {



    @Override
    public Cartao buscarPorNumero(String numero) {
        // SQL que une as 3 tabelas (join triplo) para obter todos os dados necessários de uma vez
        String sql = "SELECT " +
                     "ca.id as cartao_id, ca.numero as cartao_numero, ca.nome_impresso, ca.data_expiracao, ca.pin, " +
                     "co.id as conta_id, co.numero_conta, co.saldo_total, co.saldo_disponivel, " +
                     "cl.id as cliente_id, cl.nome, cl.email, cl.status " +
                     "FROM cartoes ca " +
                     "JOIN contas co ON ca.conta_id = co.id " +
                     "JOIN clientes cl ON co.titular_id = cl.id " +
                     "WHERE ca.numero = ?";

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setString(1, numero);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Processo de reconstrução em 3 etapas, do mais interno para o mais externo

                // 1. Reconstruir Cliente
                Cliente titular = new Cliente(rs.getString("nome"), rs.getString("email"), null, null);
                titular.atualizarStatus(StatusCliente.valueOf(rs.getString("status")));

                //titular.setId(rs.getInt("Ccliente_id"));      caso adicione ID ao cliente

                // 2. Reconstruir Conta (usando o Cliente criado acima)
                Conta conta = new Conta(rs.getString("numero_conta"), titular, rs.getDouble("saldo_total"));
                conta.setId(rs.getInt("conta_id"));

                // 3. Reconstruir Cartao (usando a Conta criada acima)
                Cartao cartao = new Cartao(
                    rs.getString("cartao_numero"),
                    rs.getString("nome_impresso"),
                    rs.getDate("data_expiracao").toLocalDate(), // Converte java.sql.Date para java.time.LocalDate
                    rs.getString("pin"),
                    conta
                );
                // cartao.setId(rs.getInt("cartao_id"));    caso adicione ID ao cartao

                return cartao;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Retorna null se não encontrou o cartão ou se ocorreu um erro
    }
}
