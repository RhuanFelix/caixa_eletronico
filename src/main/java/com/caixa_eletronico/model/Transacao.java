package com.caixa_eletronico.model;

import java.time.LocalDateTime;

public abstract class Transacao {

    // Em um sistema real, o ID seria gerado pelo banco de dados (auto-incremento)
    private long id; 
    private LocalDateTime dataCriacao;
    private String status; // Ex: "CONCLUIDA", "FALHA", "PENDENTE"
    protected Conta conta; // A conta principal envolvida na transação
    protected CaixaEletronico caixaEletronico; // O local onde a transação ocorreu

    public Transacao(Conta conta, CaixaEletronico caixaEletronico) {
        this.dataCriacao = LocalDateTime.now();
        this.status = "PENDENTE";
        this.conta = conta;
        this.caixaEletronico = caixaEletronico;
    }

    // Getters e Setters
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public String getStatus() { return status; }
    public Conta getConta() { return conta; }
    public CaixaEletronico getCaixaEletronico() { return caixaEletronico; }
}