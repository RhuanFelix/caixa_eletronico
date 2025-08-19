package com.caixa_eletronico.model;

public class Conta {

    private String numeroConta;
    private double saldoTotal;
    private double saldoDisponivel;
    private Cliente titular; // Relacionamento: a conta TEM UM titular

    public Conta(String numeroConta, Cliente titular, double depositoInicial) {
        this.numeroConta = numeroConta;
        this.titular = titular;
        
        if (depositoInicial >= 0) {
            this.saldoTotal = depositoInicial;
            this.saldoDisponivel = depositoInicial;
        } else {
            this.saldoTotal = 0;
            this.saldoDisponivel = 0;
        }
    }

    public void depositar(double valor) {
        if (valor > 0) {
            this.saldoTotal += valor;
            this.saldoDisponivel += valor;
            System.out.println("Depósito de R$" + valor + " realizado com sucesso na conta " + this.numeroConta);
        } else {
            System.out.println("Erro: O valor para depósito deve ser positivo.");
        }
    }


    //O montante a ser sacado deve ser positivo e não pode exceder o saldo disponível.
     
    public boolean sacar(double valor) {
        if (valor <= 0) {
            System.out.println("Erro: O valor para saque deve ser positivo.");
            return false;
        }

        if (valor > this.saldoDisponivel) {
            System.out.println("Erro: Saldo insuficiente para realizar o saque de R$" + valor);
            return false;
        }

        this.saldoTotal -= valor;
        this.saldoDisponivel -= valor;
        System.out.println("Saque de R$" + valor + " realizado com sucesso da conta " + this.numeroConta);
        return true;
    }


    public String getNumeroConta() {
        return numeroConta;
    }

    public double getSaldoTotal() {
        return saldoTotal;
    }

    public double getSaldoDisponivel() {
        return saldoDisponivel;
    }

    public Cliente getTitular() {
        return titular;
    }

    // Não criamos Setters para os saldos, pois eles só devem ser alterados
    // pelos métodos depositar() e sacar() para manter a integridade dos dados.
}
