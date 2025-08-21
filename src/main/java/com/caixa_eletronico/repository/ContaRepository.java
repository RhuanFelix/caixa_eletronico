package com.caixa_eletronico.repository;

import com.caixa_eletronico.model.Conta;
import java.util.List;
import java.util.Optional; // Optional para um retorno mais seguro

public interface ContaRepository {

    //Salva uma nova conta no banco de dados.

    void salvar(Conta conta);

    //Atualiza os dados de uma conta existente, principalmente o saldo.
     
    void atualizar(Conta conta);

    /**
     * Busca uma conta pelo seu número único.
     * Retorna um Optional, que pode conter a Conta se encontrada, ou estar vazio.
     * numero é O número da conta a ser buscada.
     * return um Optional<Conta> contendo a conta encontrada ou vazio.
    */
    Optional<Conta> buscarPorNumero(String numero);

    /*
     * Busca uma conta pelo seu ID de chave primária.
     * retorna um Optional<Conta> contendo a conta encontrada ou vazio.
    */
    Optional<Conta> buscarPorId(long id);

    //Retorna uma lista com todas as contas cadastradas.

    List<Conta> listarTodas();

    //Deleta uma conta do banco de dados pelo seu ID.
     
    void deletar(long id);
}