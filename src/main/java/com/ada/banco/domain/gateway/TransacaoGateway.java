package com.ada.banco.domain.gateway;

import com.ada.banco.domain.model.Transacao;

public interface TransacaoGateway {

    Transacao salvar(Transacao transacao);

}
