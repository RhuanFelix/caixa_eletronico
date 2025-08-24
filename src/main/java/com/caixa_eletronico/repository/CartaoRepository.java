package com.caixa_eletronico.repository;

import com.caixa_eletronico.model.Cartao;

public interface CartaoRepository {

    Cartao buscarPorNumero(String numero);
    
    // (futuramente, poderíamos adicionar métodos como salvar(), buscarPorId(), etc.)
}
