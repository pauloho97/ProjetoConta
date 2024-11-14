package com.example.contaapplication;

import java.util.Locale;

public class Transacoes {
    private Integer id;
    private String tipo; // "DEPOSITAR" ou "RETIRAR"
    private Float valor;

    //construtor padrão
    public Transacoes(String tipo, Float valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Float getValor() {
        return valor;
    }

    public void setValor(Float valor) {
        this.valor = valor;
    }

    @Override
    public String toString() {
        return tipo + ": R$ " + String.format(Locale.US, "%.2f", valor);
    }

}
