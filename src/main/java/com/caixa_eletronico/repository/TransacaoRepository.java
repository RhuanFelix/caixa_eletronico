package com.caixa_eletronico.repository;

import com.caixa_eletronico.model.Transacao;

public interface TransacaoRepository {

    //Salva um registro de qualquer tipo de transação (Saque, Deposito, etc.)
    public abstract void salvar(Transacao transacao);
    
    
    // Futuramente, poderíamos adicionar métodos para gerar relatórios, como:
    // List<Transacao> buscarPorConta(long contaId);
}