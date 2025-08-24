package com.caixa_eletronico.model;

import java.time.LocalDateTime;
import java.sql.PreparedStatement; 
import java.sql.SQLException;

public abstract class Transacao {

    // Em um sistema real, o ID seria gerado pelo banco de dados (auto-incremento)
    private long id; 
    private LocalDateTime dataCriacao;
    private String status; // Ex: "CONCLUIDA", "FALHA", "PENDENTE"
    protected Conta conta; // A conta principal envolvida na transação
    protected CaixaEletronico caixaEletronico; // O local onde a transação ocorreu

    public Transacao(Conta conta, CaixaEletronico caixaEletronico) {
        this.dataCriacao = LocalDateTime.now();
        this.status = "CONCLUIDA";
        this.conta = conta;
        this.caixaEletronico = caixaEletronico;
    }

    /*
     * Retorna o tipo da transação como uma String para ser salva no banco (ex: "SAQUE").
     * Cada subclasse definirá seu próprio tipo.
     */
    public abstract String getTipoTransacao();

    /*
     * Preenche os parâmetros específicos desta transação em um PreparedStatement.
     * A classe Repository vai preparar o statement, e cada transação saberá
     * como preencher suas próprias colunas (valor, conta_destino_id, etc.).
     * pstmt será o PreparedStatement a ser preenchido.
     * throws SQLException sinaliza que pode lançar exceção de SQL
     */
    public abstract void preencherParametrosEspecificos(PreparedStatement pstmt) throws SQLException;

    // Getters e Setters
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public String getStatus() { return status; }
    public Conta getConta() { return conta; }
    public CaixaEletronico getCaixaEletronico() { return caixaEletronico; }
}