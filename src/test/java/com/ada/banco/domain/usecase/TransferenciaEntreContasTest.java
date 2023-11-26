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
public class TransferenciaEntreContasTest {

    @Mock
    private ContaGateway contaGateway;

    @Mock
    private TransacaoGateway transacaoGateway;

    @InjectMocks
    private TranferenciaEntreContas tranferenciaEntreContas;

    @Test
    public void deveTransferirValorEntreContas() throws Exception {
        // Given
        Cliente clienteRemetente = new Cliente(32L, "Leonardo", "12345", "leonardo@gmail.com");
        Cliente clienteDestino = new Cliente(54L, "João", "65432", "joao@gmail.com");
        Conta contaRemetente = new Conta(1L, 123L, 456L, BigDecimal.valueOf(1000), TipoConta.CORRENTE, clienteRemetente);
        Conta contaDestino = new Conta(2L, 789L, 987L, BigDecimal.valueOf(500), TipoConta.POUPANCA, clienteDestino);
        BigDecimal valorTransferencia = BigDecimal.valueOf(200);

        Mockito.when(contaGateway.buscarContaPorId(contaRemetente.getId())).thenReturn(contaRemetente);
        Mockito.when(contaGateway.buscarContaPorId(contaDestino.getId())).thenReturn(contaDestino);
        Mockito.when(transacaoGateway.salvar(Mockito.any())).thenReturn(new Transacao(valorTransferencia, TipoTransacao.TRANSFERENCIA, contaRemetente, contaDestino));

        // When
        Transacao transacao = tranferenciaEntreContas.execute(contaRemetente, contaDestino, valorTransferencia);

        // Then
        Assertions.assertAll(
                () -> Assertions.assertEquals(TipoTransacao.TRANSFERENCIA, transacao.getTipoTransacao()),
                () -> Assertions.assertEquals(valorTransferencia, transacao.getValor()),
                () -> Assertions.assertEquals(contaDestino.getId(), transacao.getContaDestino().getId()),
                () -> Assertions.assertEquals(contaRemetente.getId(), transacao.getContaRemetente().getId()),
                () -> Assertions.assertEquals(transacao.getContaDestino().getSaldo().compareTo(BigDecimal.valueOf(700)), 0)
        );

        Mockito.verify(contaGateway, Mockito.times(1)).buscarContaPorId(contaRemetente.getId());
        Mockito.verify(contaGateway, Mockito.times(1)).buscarContaPorId(contaDestino.getId());
        Mockito.verify(contaGateway, Mockito.times(1)).salvar(contaRemetente);
        Mockito.verify(contaGateway, Mockito.times(1)).salvar(contaDestino);
        Mockito.verify(transacaoGateway, Mockito.times(1)).salvar(Mockito.any());
    }

    @Test
    public void deveLancarExcecaoQuandoContaRemetenteNaoEncontrada() {
        // Given
        Cliente clienteRemetente = new Cliente(32L, "Leonardo", "12345", "leonardo@gmail.com");
        Cliente clienteDestino = new Cliente(54L, "João", "65432", "joao@gmail.com");
        Conta contaRemetente = new Conta(1L, 123L, 456L, BigDecimal.valueOf(1000), TipoConta.CORRENTE, clienteRemetente);
        Conta contaDestino = new Conta(2L, 789L, 987L, BigDecimal.valueOf(500), TipoConta.POUPANCA, clienteDestino);
        BigDecimal valorTransferencia = BigDecimal.valueOf(200);

        Mockito.when(contaGateway.buscarContaPorId(contaRemetente.getId())).thenReturn(null);

        // When Then
        Throwable throwable = Assertions.assertThrows(
                Exception.class,
                () -> tranferenciaEntreContas.execute(contaRemetente, contaDestino, valorTransferencia));

        Assertions.assertEquals("Conta Remetente não encontrada" ,throwable.getMessage());

        Mockito.verify(contaGateway, Mockito.times(1)).buscarContaPorId(contaRemetente.getId());
        Mockito.verify(contaGateway, Mockito.never()).buscarContaPorId(contaDestino.getId());
        Mockito.verify(contaGateway, Mockito.never()).salvar(Mockito.any());
        Mockito.verify(transacaoGateway, Mockito.never()).salvar(Mockito.any());
    }

    @Test
    public void deveLancarExcecaoQuandoContaDestinoNaoEncontrada() {
        // Given
        Cliente clienteRemetente = new Cliente(32L, "Leonardo", "12345", "leonardo@gmail.com");
        Cliente clienteDestino = new Cliente(54L, "João", "65432", "joao@gmail.com");
        Conta contaRemetente = new Conta(1L, 123L, 456L, BigDecimal.valueOf(1000), TipoConta.POUPANCA, clienteRemetente);
        Conta contaDestino = new Conta(2L, 789L, 987L, BigDecimal.valueOf(500), TipoConta.POUPANCA, clienteDestino);
        BigDecimal valorTransferencia = BigDecimal.valueOf(200);

        Mockito.when(contaGateway.buscarContaPorId(contaRemetente.getId())).thenReturn(contaRemetente);
        Mockito.when(contaGateway.buscarContaPorId(contaDestino.getId())).thenReturn(null);

        // When Then
        Throwable throwable = Assertions.assertThrows(
                Exception.class,
                () -> tranferenciaEntreContas.execute(contaRemetente, contaDestino, valorTransferencia));

        Assertions.assertEquals("Conta Destino não encontrada", throwable.getMessage());

        Mockito.verify(contaGateway, Mockito.times(1)).buscarContaPorId(contaRemetente.getId());
        Mockito.verify(contaGateway, Mockito.times(1)).buscarContaPorId(contaDestino.getId());
        Mockito.verify(contaGateway, Mockito.never()).salvar(Mockito.any());
        Mockito.verify(transacaoGateway, Mockito.never()).salvar(Mockito.any());
    }

    @Test
    public void deveLancarExcecaoQuandoValorTransferenciaNegativoOuIgualAZero() {
        // Given
        Cliente clienteRemetente = new Cliente(32L, "Leonardo", "12345", "leonardo@gmail.com");
        Cliente clienteDestino = new Cliente(54L, "João", "65432", "joao@gmail.com");
        Conta contaRemetente = new Conta(1L, 123L, 456L, BigDecimal.valueOf(1000), TipoConta.POUPANCA, clienteRemetente);
        Conta contaDestino = new Conta(2L, 789L, 987L, BigDecimal.valueOf(500), TipoConta.POUPANCA, clienteDestino);
        BigDecimal valorTransferencia = BigDecimal.valueOf(-200);

        Mockito.when(contaGateway.buscarContaPorId(contaRemetente.getId())).thenReturn(contaRemetente);
        Mockito.when(contaGateway.buscarContaPorId(contaDestino.getId())).thenReturn(contaDestino);

        // When Then
        Throwable throwable = Assertions.assertThrows(
                Exception.class,
                () -> tranferenciaEntreContas.execute(contaRemetente, contaDestino, valorTransferencia));

        Assertions.assertEquals("O valor para saque deve ser maior que zero", throwable.getMessage());

        Mockito.verify(contaGateway, Mockito.times(2)).buscarContaPorId(Mockito.anyLong());
        Mockito.verify(contaGateway, Mockito.never()).salvar(Mockito.any());
        Mockito.verify(transacaoGateway, Mockito.never()).salvar(Mockito.any());
    }

    @Test
    public void deveLancarExcecaoQuandoSaldoInsuficiente() {
        // Given
        Cliente clienteRemetente = new Cliente(32L, "Leonardo", "12345", "leonardo@gmail.com");
        Cliente clienteDestino = new Cliente(54L, "João", "65432", "joao@gmail.com");
        Conta contaRemetente = new Conta(1L, 123L, 456L, BigDecimal.valueOf(1000), TipoConta.POUPANCA, clienteRemetente);
        Conta contaDestino = new Conta(2L, 789L, 987L, BigDecimal.valueOf(500), TipoConta.POUPANCA, clienteDestino);
        BigDecimal valorTransferencia = BigDecimal.valueOf(1500);

        Mockito.when(contaGateway.buscarContaPorId(contaRemetente.getId())).thenReturn(contaRemetente);
        Mockito.when(contaGateway.buscarContaPorId(contaDestino.getId())).thenReturn(contaDestino);

        // When Then
        Throwable throwable = Assertions.assertThrows(
                Exception.class,
                () -> tranferenciaEntreContas.execute(contaRemetente, contaDestino, valorTransferencia));

        Assertions.assertEquals("Saldo insuficiente na Conta Remetente", throwable.getMessage());

        Mockito.verify(contaGateway, Mockito.times(1)).buscarContaPorId(contaRemetente.getId());
        Mockito.verify(contaGateway, Mockito.times(1)).buscarContaPorId(contaDestino.getId());
        Mockito.verify(contaGateway, Mockito.never()).salvar(Mockito.any());
        Mockito.verify(transacaoGateway, Mockito.never()).salvar(Mockito.any());
    }

}
