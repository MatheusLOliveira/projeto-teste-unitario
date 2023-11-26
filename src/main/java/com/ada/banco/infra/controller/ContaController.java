package com.ada.banco.infra.controller;

import com.ada.banco.domain.model.Conta;
import com.ada.banco.domain.usecase.CriarNovaConta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/contas")
public class ContaController {

    private final CriarNovaConta criarNovaConta;

    public ContaController(CriarNovaConta criarNovaConta) {
        this.criarNovaConta = criarNovaConta;
    }

    @PostMapping
    public ResponseEntity<?> criarNovaConta(@RequestBody Conta conta) {
        Conta novaConta;
        try {
            novaConta = criarNovaConta.execute(conta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(novaConta);
    }

}
