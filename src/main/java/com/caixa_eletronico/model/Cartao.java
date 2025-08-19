package com.caixa_eletronico.model;

import java.time.LocalDate;

public class Cartao {

    private String numero;
    private String nomeImpresso;
    private LocalDate dataExpiracao; 
    private String pin; 
    private Conta conta;

    public Cartao(String numero, String nomeImpresso, LocalDate dataExpiracao, String pin, Conta conta) {
        this.numero = numero;
        this.nomeImpresso = nomeImpresso;
        this.dataExpiracao = dataExpiracao;
        this.pin = pin;
        this.conta = conta;
    }

    //  Verifica se o PIN fornecido corresponde ao PIN do cartão.
    //  Este método será chamado pela classe CaixaEletronico durante a autenticação.
     
    public boolean verificarPin(String pinTentativa) {
        return this.pin.equals(pinTentativa);
    }

    //Verifica se o cartão não está expirado.
 
    public boolean isValido() {
        // LocalDate.now() pega a data atual do sistema
        return !LocalDate.now().isAfter(this.dataExpiracao);
    }

    // --- Getters ---
    public String getNumero() {
        return numero;
    }

    public String getNomeImpresso() {
        return nomeImpresso;
    }

    public LocalDate getDataExpiracao() {
        return dataExpiracao;
    }

    public Conta getConta() {
        return conta;
    }

}
