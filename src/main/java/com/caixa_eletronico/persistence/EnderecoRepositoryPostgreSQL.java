package com.caixa_eletronico.persistence;

import com.caixa_eletronico.connection.ConexaoFactory;
import com.caixa_eletronico.model.Endereco;
import com.caixa_eletronico.repository.EnderecoRepository;

import java.sql.*;

import java.sql.Connection;
import java.sql.PreparedStatement;


public class EnderecoRepositoryPostgreSQL implements EnderecoRepository {

    // METODO PARA BUSCAR ENDEREÇO PELO ID DO CLIENTE
    public Endereco buscarPorCliente(int clienteId){
        String sql = "SELECT e.id, e.rua, e.bairro, e.numero_residencia, e.cidade, e.uf FROM endereco e WHERE e.id_cliente = ?";

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement pstmt = conexao.prepareStatement(sql)){

            pstmt.setInt(1, clienteId);
            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                Endereco endereco = new Endereco(rs.getString("rua"), rs.getString("bairro"), rs.getInt("numero_residencia"), rs.getString("cidade"), rs.getString("uf"), null);
                return endereco;
            }
        } catch (SQLException e){
            System.out.println("Erro ao consultar endereço: " + e.getMessage());
        }
        return null;
    }

    // METODO PARA INSERIR ENDEREÇO
    public void salvar(Endereco endereco){
        String sql = "INSERT INTO endereco (rua, bairro, numero_residencia, cidade, uf, id_cliente) VALUES (?,?,?,?,?,?)";

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement pstmt = conexao.prepareStatement(sql)){

            pstmt.setString(1, endereco.getRua());
            pstmt.setString(2, endereco.getBairro());
            pstmt.setInt(3, endereco.getNumeroResidencia());
            pstmt.setString(4, endereco.getCidade());
            pstmt.setString(5, endereco.getUf());
            pstmt.setInt(6, endereco.getTitular().getId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("Erro ao inserir endereço: " + e.getMessage());
        }
    }
}


