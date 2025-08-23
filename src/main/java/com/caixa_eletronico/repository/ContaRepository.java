package com.caixa_eletronico.repository;

import com.caixa_eletronico.model.Conta;
import java.util.List;

public interface ContaRepository {

    //Salva uma nova conta no banco de dados.

    void salvar(Conta conta);

    //Atualiza os dados de uma conta existente, principalmente o saldo.
     
    void atualizarSaldo(Conta conta);

    /**
     * Busca uma conta pelo seu número único.
     * numero é O número da conta a ser buscada.
     * retorna a conta ou null se não encontrada
    */
    Conta buscarPorNumero(String numero);

    /*
     * Busca uma conta pelo seu ID de chave primária.
     * retorna uma conta ou null se não encontrada.
    */
    Conta buscarPorId(long id);

    //Retorna uma lista com todas as contas cadastradas.

    List<Conta> listarTodas();

    //Deleta uma conta do banco de dados pelo seu ID.
     
    void deletar(int id);
}