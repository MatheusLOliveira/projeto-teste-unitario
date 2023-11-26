package com.ada.banco.domain.model.enums;

public enum TipoTransacao {

    DEPOSITO("Deposito"),
    SAQUE("Saque"),
    TRANSFERENCIA("Transferencia");

    private String tipoTransacao;

    private TipoTransacao(String tipoTransacao) {
        this.tipoTransacao = tipoTransacao;
    }

    public String getTipoTransacao() {
        return tipoTransacao;
    }

}
