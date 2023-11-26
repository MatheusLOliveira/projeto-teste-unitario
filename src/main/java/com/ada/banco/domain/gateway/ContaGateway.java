package com.ada.banco.domain.gateway;

import com.ada.banco.domain.model.Conta;

public interface ContaGateway {
    Conta salvar(Conta conta);

    Conta buscarContaPorId(Long id);
}
