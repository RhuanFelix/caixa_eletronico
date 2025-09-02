package com.caixa_eletronico.repository;

import com.caixa_eletronico.model.Telefone;
import java.util.List;

public interface TelefoneRepository {
    List<Telefone> listarPorCliente(int clienteId);// listar todos os telefones
    void salvar(Telefone telefone); // inserir telefone no banco
}
