package com.ada.banco.domain.model;

import com.ada.banco.domain.model.enums.TipoTransacao;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private BigDecimal valor;
    @Column(nullable = false)
    private LocalDateTime dataTransacao;
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipoTransacao;

    @OneToOne
    private Conta contaRemetente;

    @OneToOne
    private Conta contaDestino;

    public Transacao(BigDecimal valor, TipoTransacao tipoTransacao, Conta contaRemetente, Conta contaDestino) {
        this.valor = valor;
        this.dataTransacao = LocalDateTime.now();
        this.tipoTransacao = tipoTransacao;
        this.contaRemetente = contaRemetente;
        this.contaDestino = contaDestino;
    }

    public Transacao(BigDecimal valor, TipoTransacao tipoTransacao, Conta contaRemetente) {
        this.valor = valor;
        this.dataTransacao = LocalDateTime.now();
        this.tipoTransacao = tipoTransacao;
        this.contaRemetente = contaRemetente;
    }

    public Transacao() {
    }

    public BigDecimal getValor() {
        return valor;
    }

    public TipoTransacao getTipoTransacao() {
        return tipoTransacao;
    }

    public Conta getContaRemetente() {
        return contaRemetente;
    }

    public Conta getContaDestino() {
        return contaDestino;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transacao transacao = (Transacao) o;
        return Objects.equals(id, transacao.id) && Objects.equals(valor, transacao.valor) && Objects.equals(dataTransacao, transacao.dataTransacao) && tipoTransacao == transacao.tipoTransacao && Objects.equals(contaRemetente, transacao.contaRemetente) && Objects.equals(contaDestino, transacao.contaDestino);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, valor, dataTransacao, tipoTransacao, contaRemetente, contaDestino);
    }
}
