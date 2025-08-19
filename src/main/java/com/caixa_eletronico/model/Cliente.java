package com.caixa_eletronico.model;

public class Cliente {

    // --- Atributos ---
    private String nome;
    private String email;
    private String telefone;
    private String endereco;
    private StatusCliente status;

    public Cliente(String nome, String email, String telefone, String endereco) {
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
    
    public String getNome() {
        return nome;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public StatusCliente getStatus() {
        return status;
    }
}
