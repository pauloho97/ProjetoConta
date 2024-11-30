package com.example.contaapplication;

public class Pix {
    private Integer id;
    private String chave;
    private String valorChave;

    //  padrão
    public Pix() {
    }


    public Pix(Integer id, String chave, String valorChave) {
        this.id = id;
        this.chave = chave;
        this.valorChave = valorChave;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Tipo da Chave: " + valorChave + "\n" +
                "Chave: " + chave +
                (id != null ? "\nID: " + id : "");
    }


    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }


    public String getValorChave() {
        return valorChave;
    }

    public void setValorChave(String valorChave) {
        this.valorChave = valorChave;
    }
}
