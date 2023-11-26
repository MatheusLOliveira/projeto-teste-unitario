package com.ada.banco.domain.usecase;

import com.ada.banco.domain.gateway.ClienteGateway;
import com.ada.banco.domain.gateway.ContaGateway;
import com.ada.banco.domain.model.Cliente;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.enums.TipoConta;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

@ExtendWith(MockitoExtension.class)
public class CriarNovaContaTest {

    @Mock
    ContaGateway contaGateway;

    @InjectMocks
    private CriarNovaConta criarNovaConta;

    @Test
    public void deveLancarExceptionCasoAContaJaExista() {
        // Given
        Cliente cliente = new Cliente(10L, "Matheus", "12345", "matheus@teste.com");
        Conta conta = new Conta(1L, 2L, 3L, BigDecimal.ZERO, TipoConta.POUPANCA, cliente);

        Mockito.when(contaGateway.buscarContaPorId(conta.getId())).thenReturn(conta);

        // When Then
        Throwable throwable = Assertions.assertThrows(
                Exception.class,
                () -> criarNovaConta.execute(conta)
        );

        Assertions.assertEquals("Essa conta já existe", throwable.getMessage());

        Mockito.verify(contaGateway, Mockito.times(1)).buscarContaPorId(conta.getId());
        Mockito.verify(contaGateway, Mockito.never()).salvar(Mockito.any());
    }

    @Test
    public void deveCriarNovaContaQuandoClienteNaoPossuiConta() throws Exception {
        // Given
        Cliente cliente = new Cliente(43L, "João", "1234556", "joao@gmail.com");
        Conta conta = new Conta(10L, 1L, 1234L, BigDecimal.ZERO, TipoConta.POUPANCA, cliente);

        Mockito.when(contaGateway.buscarContaPorId(conta.getId())).thenReturn(null);
        Mockito.when(contaGateway.salvar(conta)).thenReturn(conta);

        // When
        Conta novaConta = criarNovaConta.execute(conta);

        // Then
        Assertions.assertEquals(conta, novaConta);

        Assertions.assertAll(
                () -> Assertions.assertEquals(10L, novaConta.getId()),
                () -> Assertions.assertEquals("João", novaConta.getCliente().getNome())
        );

        Mockito.verify(contaGateway, Mockito.times(1)).buscarContaPorId(conta.getId());
        Mockito.verify(contaGateway, Mockito.times(1)).salvar(conta);
    }

}
