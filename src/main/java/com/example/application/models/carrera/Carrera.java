package com.example.application.models.carrera;

import java.util.List;

public class Carrera {
    private String nombre;
    private String tipo;
    private List<Materia> materias;

    public Carrera(String nombre, String tipo, List<Materia> materias) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.materias = materias;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<Materia> getMaterias() {
        return materias;
    }

    public void setMaterias(List<Materia> materias) {
        this.materias = materias;
    }
}