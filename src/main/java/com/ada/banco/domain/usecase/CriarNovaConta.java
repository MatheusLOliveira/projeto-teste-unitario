package com.ada.banco.domain.usecase;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.model.Conta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CriarNovaConta {

    @Autowired
    private ContaGateway contaGateway;

    public Conta execute(Conta conta) throws Exception {
        if (contaGateway.buscarContaPorId(conta.getId()) != null) {
            throw new Exception("Essa conta já existe");
        }

        return contaGateway.salvar(conta);
    }

}
