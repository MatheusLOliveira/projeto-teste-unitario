package com.ada.banco.domain.gateway;

import com.ada.banco.domain.model.Cliente;

public interface ClienteGateway {

    Cliente buscarPorCpf(String cpf);

    Cliente salvar(Cliente cliente);

}
