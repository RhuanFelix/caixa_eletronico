package com.caixa_eletronico.persistence;

import com.caixa_eletronico.model.*;
import com.caixa_eletronico.repository.ContaRepository;
import java.util.List;
import java.sql.*;

public class ContaRepositoryPostrgeSQL implements ContaRepository{
    @Override
    public Conta buscarPorNumero(String numero) {
        String sql = "SELECT c.id as conta_id, c.numero_conta, c.saldo_total, c.saldo_disponivel, " +
                     "cl.id as cliente_id, cl.nome, cl.email, cl.telefone, cl.endereco, cl.status " +
                     "FROM contas c JOIN clientes cl ON c.titular_id = cl.id WHERE c.numero_conta = ?";

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {
            
            pstmt.setString(1, numero);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Primeiro, extraímos cada valor da linha retornada pelo banco
                String nomeCliente = rs.getString("nome");
                String emailCliente = rs.getString("email");
                String telefoneCliente = rs.getString("telefone");
                String enderecoCliente = rs.getString("endereco");

                // criando o objeto Cliente baseado nos dados da consulta
                Cliente titular = new Cliente(nomeCliente, emailCliente, telefoneCliente, enderecoCliente);

                // criando o objeto Conta baseado nos dados da consulta
                Conta conta = new Conta(rs.getString("numero_conta"), titular, rs.getDouble("saldo_total"));
                
                // Preenchendo o ID na conta que veio do banco
                conta.setId(rs.getInt("conta_id")); 
                
                return conta;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null; // Retorna null se não encontrou ou se deu erro
    }

    @Override
    public void atualizarSaldo(Conta conta) {
        String sql = "UPDATE contas SET saldo_total = ?, saldo_disponivel = ? WHERE id = ?";

        try (Connection conexao = ConexaoFactory.criarConexao();
            PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setDouble(1, conta.getSaldoTotal());
            pstmt.setDouble(2, conta.getSaldoDisponivel());
            pstmt.setInt(3, conta.getId()); // Usa o ID para encontrar a linha certa

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // outros métodos da interface a serem implementados ...
    @Override
    public void salvar(Conta conta) { /* implementacao */ }

    @Override
    public Conta buscarPorId(long id) { /* implementacao */ return null; }

    @Override
    public List<Conta> listarTodas() { /* implementacao */ return null;}

    @Override
    public void deletar(int id) {/* implementacao */};

}
