package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.Cliente;
import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.model.enums.TipoConta;
import com.ada.banco.infra.gateway.bd.interfaces.ClienteRepository;
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
public class ClienteControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteController clienteController;

    @BeforeEach
    void beforeEach() {
        clienteRepository.deleteAll();
    }

    // TODO: Arrumar o usecase para salvar
//    @Test
//    void criarCliente_ComSucesso_DeveRetornarStatus201() throws Exception {
//        // Arrange
//        Cliente cliente = new Cliente(46L, "João", "123455678908", "joao123@gmail.com");
//        String requestBody = objectMapper.writeValueAsString(cliente);
//
//        // Act & Assert
//        mockMvc.perform(MockMvcRequestBuilders
//                        .post("/api/v1/cliente")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(requestBody))
//                .andExpect(MockMvcResultMatchers.status().isCreated());
//
//        Cliente clienteNovo = clienteRepository.findByCpf("123455678908");
//        Assertions.assertNotNull(clienteNovo);
//    }

    @Test
    void criarCliente_JaExistente_DeveRetornarStatusBadRequest() throws Exception {
        // Arrange
        Cliente cliente = new Cliente(43L, "João", "1234556", "joao@gmail.com");
        String requestBody = objectMapper.writeValueAsString(cliente);

        clienteRepository.save(cliente);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

        Cliente clienteTeste = clienteRepository.findByCpf("1234556");
        Assertions.assertNotNull(clienteTeste);
    }

    @Test
    void criarCliente_ComSucesso_DeveSalvarOCliente() throws Exception {
        Cliente cliente = new Cliente(4343L, "João", "1235344556", "joao@gmail.com");

        // when
        clienteController.criarNovoCliente(cliente);

        // then
        Cliente clienteCriado = clienteRepository.findByCpf("1235344556");
        Assertions.assertEquals("João", clienteCriado.getNome());
    }

}
