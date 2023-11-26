package com.ada.banco.domain.usecase;

import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.gateway.TransacaoGateway;
import com.ada.banco.domain.model.Cliente;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.Transacao;
import com.ada.banco.domain.model.enums.TipoConta;
import com.ada.banco.domain.model.enums.TipoTransacao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class DepositarTest {

    @Mock
    private ContaGateway contaGateway;

    @Mock
    private TransacaoGateway transacaoGateway;

    @InjectMocks
    private Depositar depositar;

    @Test
    public void deveDepositarCorretamente() throws Exception {
        // Given
        Cliente cliente = new Cliente(43L, "João", "1234556", "joao@gmail.com");
        Conta conta = new Conta(1L, 123L, 456L, BigDecimal.valueOf(1000), TipoConta.POUPANCA, cliente);

        BigDecimal valorDeposito = BigDecimal.valueOf(500);

        Mockito.when(contaGateway.buscarContaPorId(conta.getId())).thenReturn(conta);
        Mockito.when(transacaoGateway.salvar(Mockito.any())).thenReturn(new Transacao(valorDeposito, TipoTransacao.DEPOSITO, conta));

        // When
        Transacao transacao = depositar.execute(conta, valorDeposito);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(conta.getSaldo(), transacao.getContaRemetente().getSaldo()), ///// dsa
                () -> Assertions.assertEquals(TipoTransacao.DEPOSITO, transacao.getTipoTransacao()),
                () -> Assertions.assertEquals(valorDeposito, transacao.getValor()),
                () -> Assertions.assertEquals(transacao.getContaRemetente(), conta)
        );

        Mockito.verify(contaGateway, Mockito.times(1)).buscarContaPorId(conta.getId());
        Mockito.verify(contaGateway, Mockito.times(1)).salvar(conta);
        Mockito.verify(transacaoGateway, Mockito.times(1)).salvar(Mockito.any());
    }

    @Test
    public void deveLancarExcecaoQuandoContaNaoEncontrada() {
        // Given
        Cliente cliente = new Cliente(43L, "João", "1234556", "joao@gmail.com");
        Conta conta = new Conta(1L, 123L, 456L, BigDecimal.valueOf(1000), TipoConta.CORRENTE, cliente);
        BigDecimal valorDeposito = BigDecimal.valueOf(500);

        Mockito.when(contaGateway.buscarContaPorId(conta.getId())).thenReturn(null);

        // When Then
        Throwable throwable = Assertions.assertThrows(
                Exception.class,
                () -> depositar.execute(conta, valorDeposito));

        Assertions.assertEquals("Conta não encontrada", throwable.getMessage());

        Mockito.verify(contaGateway, Mockito.times(1)).buscarContaPorId(conta.getId());
        Mockito.verify(contaGateway, Mockito.never()).salvar(Mockito.any());
        Mockito.verify(transacaoGateway, Mockito.never()).salvar(Mockito.any());
    }

    @Test
    public void deveLancarExcecaoQuandoValorDepositoNegativoOuIgualAZero() {
        // Given
        Cliente cliente = new Cliente(43L, "João", "1234556", "joao@gmail.com");
        Conta conta = new Conta(1L, 123L, 456L, BigDecimal.valueOf(1000), TipoConta.POUPANCA, cliente);
        BigDecimal valorDeposito = BigDecimal.valueOf(-500);

        Mockito.when(contaGateway.buscarContaPorId(conta.getId())).thenReturn(conta);

        // When Then
        Throwable throwable = Assertions.assertThrows(
                Exception.class,
                () -> depositar.execute(conta, valorDeposito));

        Assertions.assertEquals("O valor do depósito é inferior ou igual a 0", throwable.getMessage());

        Mockito.verify(contaGateway, Mockito.times(1)).buscarContaPorId(Mockito.anyLong());
        Mockito.verify(contaGateway, Mockito.never()).salvar(Mockito.any());
        Mockito.verify(transacaoGateway, Mockito.never()).salvar(Mockito.any());
    }

}
