package com.caixa_eletronico.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexaoFactory {

    // --- DADOS DE CONEXÃO ---
    
    private static final String URL = "jdbc:postgresql://localhost:5432/caixa_eletronico";
    private static final String USUARIO = "postgres"; 
    private static final String SENHA = "12345";

    /**
     * Método estático para obter uma conexão com o banco de dados.
     * return um objeto Connection pronto para ser usado.
     * throws RuntimeException se a conexão falhar.
     */

    public static Connection criarConexao() {
        try {
            // O DriverManager utiliza o driver que o Maven baixou para encontrar
            // a classe correta e estabelecer a conexão.
            return DriverManager.getConnection(URL, USUARIO, SENHA);

        } catch (SQLException e) {
            // Em caso de falha, lança uma RuntimeException para interromper a aplicação,
            // pois sem o banco de dados, o sistema não pode funcionar.
            throw new RuntimeException("Erro ao conectar com o banco de dados: " + e.getMessage(), e);
        }
    }
}
