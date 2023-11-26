package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.Cliente;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.enums.TipoConta;
import com.ada.banco.infra.gateway.bd.interfaces.ClienteRepository;
import com.ada.banco.infra.gateway.bd.interfaces.ContaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
public class ContaControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContaController contaController;
    @Autowired
    private ClienteController clienteController;

    @BeforeEach
    void beforeEach() {
        clienteRepository.deleteAll();
        contaRepository.deleteAll();
    }

    // TODO: Arrumar o usecase para salvar
//    @Test
//    void criarConta_ComSucesso_DeveRetornarStatus201() throws Exception {
//        // Arrange
//        Cliente cliente = new Cliente(46L, "João", "123455678908", "joao123@gmail.com");
//        Conta novaConta = new Conta(123L, 2321L, 3123L, BigDecimal.ZERO, TipoConta.CORRENTE, cliente);
//        String requestBody = objectMapper.writeValueAsString(novaConta);
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/v1/contas")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(MockMvcResultMatchers.status().isCreated());
//
//        Optional<Conta> conta = contaRepository.findById(123L);
//        Assertions.assertNotNull(conta);
//    }

    @Test
    void criarConta_JaExistente_DeveRetornarStatusBadRequest() throws Exception {
        // Arrange
        Cliente cliente = new Cliente(43L, "João", "1234556", "joao@gmail.com");
        Conta conta = new Conta(1L, 2L, 3L, BigDecimal.ZERO, TipoConta.CORRENTE, cliente);
        String requestBody = objectMapper.writeValueAsString(conta);

        clienteRepository.save(cliente);
        contaRepository.save(conta);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/contas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Optional<Conta> contaTeste = contaRepository.findById(123L);
        Assertions.assertNotNull(contaTeste);
    }

//    @Test
//    void criarConta_ComSucesso_DeveSalvarAConta() throws Exception {
//        Cliente cliente = new Cliente(4343L, "João", "1235344556", "joao@gmail.com");
//        Conta conta = new Conta(433L, 2L, 3L, BigDecimal.ZERO, TipoConta.CORRENTE, cliente);
//
//        // when
//        clienteController.criarNovoCliente(cliente);
//        contaController.criarNovaConta(conta);
//
//        // then
//        Optional<Conta> contaCriada = contaRepository.findById(433L);
//        Assertions.assertTrue(contaCriada.isPresent());
//        Assertions.assertEquals("João", contaCriada.get().getCliente().getNome());
//    }

}
