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
public class Depositar {

    @Autowired
    private ContaGateway contaGateway;
    @Autowired
    private TransacaoGateway transacaoGateway;

    public Transacao execute(Conta conta, BigDecimal valor) throws Exception {

        // Validações da conta
        if (contaGateway.buscarContaPorId(conta.getId()) == null) {
            throw new Exception("Conta não encontrada");
        }

        // Validação da transação
        if(valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("O valor do depósito é inferior ou igual a 0");
        }

        // Contas
        conta.setSaldo(conta.getSaldo().add(valor));
        contaGateway.salvar(conta);

        // Transacao
        Transacao transacao = new Transacao(valor, TipoTransacao.DEPOSITO, conta);
        return transacaoGateway.salvar(transacao);

    }

}
