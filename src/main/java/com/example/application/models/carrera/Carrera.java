package com.example.application.models.carrera;

import java.util.List;

public class Carrera {
    private String nombre;
    private String tipo;
    private List<Materia> materias;
    private int duracion; // Duration in semesters or years

    public Carrera(String nombre, String tipo, List<Materia> materias, int duracion) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.materias = materias;
        this.duracion = duracion;
    }

    // Existing constructor for backward compatibility
    public Carrera(String nombre, String tipo, List<Materia> materias) {
        this(nombre, tipo, materias, 0);
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

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }
}