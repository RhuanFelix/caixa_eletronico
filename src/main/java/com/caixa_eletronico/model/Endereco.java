package com.caixa_eletronico.model;

public class Endereco {

    private int id;                 // id do endereço (chave primária)
    private String rua;
    private String bairro;
    private int numeroResidencia;
    private String cidade;
    private String uf;
    private Cliente titular;          // referência ao cliente dono do endereço

    // Construtor completo
    public Endereco(String rua, String bairro, int numeroResidencia, String cidade, String uf, Cliente titular) {
        this.id = id;
        this.rua = rua;
        this.bairro = bairro;
        this.numeroResidencia = numeroResidencia;
        this.cidade = cidade;
        this.uf = uf;
        this.titular = titular;
    }


    // --- Getters e Setters ---


    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public int getNumeroResidencia() { return numeroResidencia; }
    public void setNumeroResidencia(int numeroResidencia) { this.numeroResidencia = numeroResidencia; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }

    public Cliente getTitular() {
        return titular;
    }
    public void setTitular(Cliente titular) {
        this.titular = titular;
    }

    // Opcional: sobrescreve o toString para facilitar debug
    @Override
    public String toString() {
        return rua + ", " + numeroResidencia + ", " + bairro + ", " + cidade + " - " + uf;
    }
}
