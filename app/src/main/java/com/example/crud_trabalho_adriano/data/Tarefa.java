package com.example.crud_trabalho_adriano.data;

import java.io.Serializable;

public class Tarefa implements Serializable {

    private int id;
    private String texto;

    public Tarefa(int id, String texto){
        this.id = id;
        this.texto = texto;
    }

    public Tarefa(String texto){
        this.texto = texto;
    }

    public int getId() {
        return id;
    }

    public String getTexto() {
        return texto;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String toString(){
        return texto;
    }
}
