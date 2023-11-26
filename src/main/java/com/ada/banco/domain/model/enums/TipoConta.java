package com.ada.banco.domain.model.enums;

public enum TipoConta {

    CORRENTE("Corrente"),
    POUPANCA("Poupanca");

    private String tipoConta;

    private TipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    public String getTipoConta() {
        return tipoConta;
    }

}
