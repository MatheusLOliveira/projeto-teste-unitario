package com.ada.banco.domain.usecase;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.TransacaoGateway;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.Transacao;
import com.ada.banco.domain.model.enums.TipoTransacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TranferenciaEntreContas {

    @Autowired
    private ContaGateway contaGateway;
    @Autowired
    private TransacaoGateway transacaoGateway;

    public Transacao execute(Conta contaRemetente, Conta contaDestino, BigDecimal valor) throws Exception {
        // Validações das contas
        if (contaGateway.buscarContaPorId(contaRemetente.getId()) == null) {
            throw new Exception("Conta Remetente não encontrada");
        }

        if (contaGateway.buscarContaPorId(contaDestino.getId()) == null) {
            throw new Exception("Conta Destino não encontrada");
        }

        // Validação da transação
        if(valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("O valor para saque deve ser maior que zero");
        }

        if(contaRemetente.getSaldo().compareTo(valor) < 0) {
            throw new Exception("Saldo insuficiente na Conta Remetente");
        }

        // Contas
        contaRemetente.setSaldo(contaRemetente.getSaldo().subtract(valor));
        contaGateway.salvar(contaRemetente);
        contaDestino.setSaldo(contaDestino.getSaldo().add(valor));
        contaGateway.salvar(contaDestino);

        // Transacao
        Transacao transacao = new Transacao(valor, TipoTransacao.TRANSFERENCIA, contaRemetente, contaDestino);
        return transacaoGateway.salvar(transacao);
    }

}
