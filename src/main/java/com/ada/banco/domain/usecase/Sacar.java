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
public class Sacar {

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
            throw new Exception("O valor para saque deve ser maior que zero");
        }

        if(conta.getSaldo().compareTo(valor) < 0) {
            throw new Exception("Saldo insuficiente");
        }

        conta.setSaldo(conta.getSaldo().subtract(valor));
        contaGateway.salvar(conta);

        Transacao transacao = new Transacao(valor, TipoTransacao.SAQUE, conta);
        return transacaoGateway.salvar(transacao);

    }

}
