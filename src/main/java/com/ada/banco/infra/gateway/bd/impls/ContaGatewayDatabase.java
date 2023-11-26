package com.ada.banco.infra.gateway.bd.impls;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.infra.gateway.bd.interfaces.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContaGatewayDatabase implements ContaGateway {

    @Autowired
    private ContaRepository contaRepository;

    @Override
    public Conta salvar(Conta conta) {
        return contaRepository.save(conta);
    }

    @Override
    public Conta buscarContaPorId(Long id) {
        return contaRepository.getById(id);
    }


}
