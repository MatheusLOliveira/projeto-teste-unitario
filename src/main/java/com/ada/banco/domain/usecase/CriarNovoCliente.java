package com.ada.banco.domain.usecase;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.model.Cliente;
import com.ada.banco.infra.gateway.bd.interfaces.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CriarNovoCliente {

    @Autowired
    private ClienteGateway clienteGateway;


    public Cliente execute(Cliente cliente) throws Exception {
        if (this.clienteGateway.buscarPorCpf(cliente.getCpf()) != null) {
            throw new Exception("Esse usuário já existe");
        }

        return this.clienteGateway.salvar(cliente);
    }

}
