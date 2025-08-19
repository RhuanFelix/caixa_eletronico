package com.caixa_eletronico.model;

public class Gerente {

    private long id; // Identificador único, virá do banco de dados
    private String nome;
    private String matricula; 

    public Gerente(long id, String nome, String matricula) {
        this.id = id;
        this.nome = nome;
        this.matricula = matricula;
    }


    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getMatricula() {
        return matricula;
    }
}
