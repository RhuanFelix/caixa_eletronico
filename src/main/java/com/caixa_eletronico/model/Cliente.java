package com.caixa_eletronico.model;

import java.util.ArrayList;
import java.util.List;

public class Cliente {

    // --- Atributos ---
    private int id;
    private String nome;
    private String email;
    private List<Telefone> telefone;
    private Endereco endereco;
    private StatusCliente status;

    public Cliente(String nome, String email, List<Telefone> telefone, Endereco endereco) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.endereco = endereco;
        this.status = StatusCliente.ATIVO; // Define o status padrão como ATIVO
    }

    //Verifica se o cliente está em um status que permite realizar transações.
     
    public boolean podeRealizarTransacao() {
        if (this.status == StatusCliente.ATIVO) {
            return true;
        } else {
            // Informa o motivo da recusa
            System.out.println("Operação não permitida. Status do cliente: " + this.status);
            return false;
        }
    }
    
    //Atualiza o status do cliente para um novo valor.
     
    public void atualizarStatus(StatusCliente novoStatus) {
        this.status = novoStatus;
        System.out.println("Status do cliente '" + this.nome + "' foi atualizado para: " + novoStatus);
    }

    // --- Getters e Setters ---


    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

  
    public List<Telefone> getTelefone() {
        return telefone;
    }

    public void adicionarTelefone(Telefone t) {
        this.telefone.add(t);
    }
    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public StatusCliente getStatus() {
        return status;
    }
}
