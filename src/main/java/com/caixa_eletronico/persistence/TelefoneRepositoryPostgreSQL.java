package com.caixa_eletronico.persistence;

import com.caixa_eletronico.connection.ConexaoFactory;
import com.caixa_eletronico.model.Telefone;
import com.caixa_eletronico.repository.TelefoneRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TelefoneRepositoryPostgreSQL implements TelefoneRepository {




    // metodo para inserir um novo telefone no banco
    @Override
    public void salvar(Telefone telefone) {
        String sql = "INSERT INTO telefone (ddd, numero, id_cliente) VALUES (?, ?, ?)";

        try (Connection conexao = ConexaoFactory.criarConexao(); // obtém uma conexão ativa com o banco
             PreparedStatement pstmt = conexao.prepareStatement(sql)) { // cria um PreparedStatement que prepara o comando SQL usando a conexão

            // preenchendo os placeholders '?' com os valores do objeto Telefone
            pstmt.setString(1, telefone.getDdd());
            pstmt.setString(2, telefone.getNumero());
            pstmt.setInt(3, telefone.getTitular().getId()); // pega o id do obj na memoria

            pstmt.executeUpdate(); // executa a insercao no banco

        } catch (SQLException e) {
            System.out.println("erro ao salvar telefone: " + e.getMessage());
        }
    }

    // METODO PARA LISTAR TELEFONES DO CLIENTE PELO ID
    public List<Telefone> listarPorCliente(int clienteId) {
        List<Telefone> telefones = new ArrayList<>();
        String sql = "SELECT t.id, t.ddd, t.numero FROM telefone t WHERE t.id_cliente = ?";

        try (Connection conexao = ConexaoFactory.criarConexao();
             PreparedStatement pstmt = conexao.prepareStatement(sql)) {

            pstmt.setInt(1, clienteId); // substitui '?' pelo ID do cliente
            ResultSet rs = pstmt.executeQuery(); //

            while (rs.next()) {
                // cria objeto Telefone sem associar o Cliente, pois ja sabemos o ID
                Telefone telefone = new Telefone(rs.getString("ddd"), rs.getString("numero"), null);
                telefones.add(telefone);
            }

        } catch (SQLException e) {
            System.out.println("erro ao listar telefones por cliente: " + e.getMessage());
        }

        return telefones;
    }

}
