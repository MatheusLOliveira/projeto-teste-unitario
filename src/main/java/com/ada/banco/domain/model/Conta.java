package com.ada.banco.domain.model;

import com.ada.banco.domain.model.enums.TipoConta;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long agencia;
    @Column(nullable = false)
    private Long digito;
    @Column(nullable = false)
    private BigDecimal saldo;
    @Enumerated(EnumType.STRING)
    private TipoConta tipoConta;

    @OneToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    public Conta() {
    }

    public Conta(Long id, Long agencia, Long digito, BigDecimal saldo, TipoConta tipoConta, Cliente cliente) {
        this.id = id;
        this.agencia = agencia;
        this.digito = digito;
        this.saldo = saldo;
        this.tipoConta = tipoConta;
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conta conta = (Conta) o;
        return Objects.equals(id, conta.id) && Objects.equals(agencia, conta.agencia) && Objects.equals(digito, conta.digito) && Objects.equals(saldo, conta.saldo) && tipoConta == conta.tipoConta && Objects.equals(cliente, conta.cliente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, agencia, digito, saldo, tipoConta, cliente);
    }
}
