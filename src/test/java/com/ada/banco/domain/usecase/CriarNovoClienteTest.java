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
public class CriarNovoClienteTest {

    @Mock
    ClienteGateway clienteGateway;

    @InjectMocks
    private CriarNovoCliente criarNovoCliente;

    @Test
    public void deveLancarExceptionCasoOClienteJaExista() {
        // Given
        Cliente cliente = new Cliente(10L, "Matheus", "12345", "matheus@teste.com");

        Mockito.when(clienteGateway.buscarPorCpf(cliente.getCpf())).thenReturn(cliente);

        // When Then
        Throwable throwable = Assertions.assertThrows(
                Exception.class,
                () -> criarNovoCliente.execute(cliente)
        );

        Assertions.assertEquals("Esse usuário já existe", throwable.getMessage());

        Mockito.verify(clienteGateway, Mockito.times(1)).buscarPorCpf(cliente.getCpf());
        Mockito.verify(clienteGateway, Mockito.never()).salvar(Mockito.any());
    }

    @Test
    public void deveCriarNovaContaQuandoClienteNaoPossuiConta() throws Exception {
        // Given
        Cliente cliente = new Cliente(43L, "João", "1234556", "joao@gmail.com");

        Mockito.when(clienteGateway.buscarPorCpf(cliente.getCpf())).thenReturn(null);
        Mockito.when(clienteGateway.salvar(cliente)).thenReturn(cliente);

        // When
        Cliente novoCliente = criarNovoCliente.execute(cliente);

        // Then
        Assertions.assertEquals(cliente, novoCliente);

        Assertions.assertAll(
                () -> Assertions.assertEquals(43L, novoCliente.getId()),
                () -> Assertions.assertEquals("João", novoCliente.getNome())
        );

        Mockito.verify(clienteGateway, Mockito.times(1)).buscarPorCpf(cliente.getCpf());
        Mockito.verify(clienteGateway, Mockito.times(1)).salvar(cliente);
    }

}
