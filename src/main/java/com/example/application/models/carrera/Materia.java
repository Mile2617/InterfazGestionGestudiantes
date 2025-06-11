package com.example.application.models.carrera;

public class Materia {
    private String nombre;
    private String docente;
    private int horas;

    public Materia(String nombre, String docente, int horas) {
        this.nombre = nombre;
        this.docente = docente;
        this.horas = horas;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }

    public int getHoras() {
        return horas;
    }

    public void setHoras(int horas) {
        this.horas = horas;
    }
}