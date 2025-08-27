package com.caixa_eletronico.repository;

import com.caixa_eletronico.model.Endereco;
import java.util.List;

public interface EnderecoRepository {
    Endereco buscarPorCliente(int clienteId); // buscar endereco pelo id de cliente
    void salvar(Endereco endereco);   // inserir endereco no banco
}
