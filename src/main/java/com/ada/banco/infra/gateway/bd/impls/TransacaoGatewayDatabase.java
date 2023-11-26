package com.ada.banco.infra.gateway.bd.impls;

import com.ada.banco.domain.gateway.TransacaoGateway;
import com.ada.banco.domain.model.Transacao;
import com.ada.banco.infra.gateway.bd.interfaces.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransacaoGatewayDatabase implements TransacaoGateway {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Override
    public Transacao salvar(Transacao transacao) {
        return transacaoRepository.save(transacao);
    }
}
