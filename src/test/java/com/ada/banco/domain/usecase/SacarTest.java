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
public class SacarTest {

    @Mock
    private ContaGateway contaGateway;

    @Mock
    private TransacaoGateway transacaoGateway;

    @InjectMocks
    private Sacar sacar;

    @Test
    public void deveSacarCorretamente() throws Exception {
        // Given
        Cliente cliente = new Cliente(43L, "João", "1234556", "joao@gmail.com");
        Conta conta = new Conta(1L, 123L, 456L, BigDecimal.valueOf(1000), TipoConta.CORRENTE, cliente);
        BigDecimal valorSaque = BigDecimal.valueOf(500);

        Mockito.when(contaGateway.buscarContaPorId(conta.getId())).thenReturn(conta);
        Mockito.when(transacaoGateway.salvar(Mockito.any())).thenReturn(new Transacao(valorSaque, TipoTransacao.SAQUE, conta));

        // When
        Transacao transacao = sacar.execute(conta, valorSaque);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(conta.getSaldo(), transacao.getContaRemetente().getSaldo()),
                () -> Assertions.assertEquals(TipoTransacao.SAQUE, transacao.getTipoTransacao()),
                () -> Assertions.assertEquals(valorSaque, transacao.getValor()),
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
        Assertions.assertThrows(Exception.class, () -> sacar.execute(conta, valorDeposito));

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
                () -> sacar.execute(conta, valorDeposito));

        Assertions.assertEquals("O valor para saque deve ser maior que zero", throwable.getMessage());

        Mockito.verify(contaGateway, Mockito.times(1)).buscarContaPorId(Mockito.anyLong());
        Mockito.verify(contaGateway, Mockito.never()).salvar(Mockito.any());
        Mockito.verify(transacaoGateway, Mockito.never()).salvar(Mockito.any());
    }

    @Test
    public void deveLancarExcecaoQuandoSaldoInsuficienteParaSacar() {
        // Given
        Cliente cliente = new Cliente(43L, "João", "1234556", "joao@gmail.com");
        Conta conta = new Conta(1L, 123L, 456L, BigDecimal.valueOf(100), TipoConta.POUPANCA, cliente);
        BigDecimal valorSacado = BigDecimal.valueOf(500);

        Mockito.when(contaGateway.buscarContaPorId(conta.getId())).thenReturn(conta);

        // When Then
        Throwable throwable = Assertions.assertThrows(
                Exception.class,
                () -> sacar.execute(conta, valorSacado));

        Assertions.assertEquals("Saldo insuficiente", throwable.getMessage());

        Mockito.verify(contaGateway, Mockito.times(1)).buscarContaPorId(Mockito.anyLong());
        Mockito.verify(contaGateway, Mockito.never()).salvar(Mockito.any());
        Mockito.verify(transacaoGateway, Mockito.never()).salvar(Mockito.any());
    }

}
