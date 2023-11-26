package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.Cliente;
import com.ada.banco.domain.usecase.CriarNovoCliente;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController {

    private final CriarNovoCliente criarNovoCliente;

    public ClienteController(CriarNovoCliente criarNovoCliente) {
        this.criarNovoCliente = criarNovoCliente;
    }

    @PostMapping
    public ResponseEntity<?> criarNovoCliente(@RequestBody @Validated Cliente cliente) {
        Cliente novoCliente;
        try {
            novoCliente = criarNovoCliente.execute(cliente);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(novoCliente);
    }

}
