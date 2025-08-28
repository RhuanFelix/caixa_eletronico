package com.caixa_eletronico.model;

public class Telefone {
    private int id;
    private String ddd;
    private String numero;
    private Cliente titular;

    public Telefone(String ddd, String numero, Cliente titular) {
        this.ddd = ddd;
        this.numero = numero;
        this.titular = titular;
    }

    public String getDdd() { return ddd; }
    public String getNumero() { return numero; }

    public Cliente getTitular() {
        return titular;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

