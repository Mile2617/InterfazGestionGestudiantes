package com.example.application.controllers;

import com.example.application.models.carrera.Carrera;
import com.example.application.models.carrera.Materia;
import com.example.application.models.persona.Estudiante;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SistemaGestionEstudiantes {
    private final List<Estudiante> estudiantes = new ArrayList<>();
    private final List<Carrera> carreras = new ArrayList<>();
    private final List<Materia> materias = new ArrayList<>();

    // Estudiante methods
    public void agregarEstudiante(Estudiante estudiante) {
        estudiantes.add(estudiante);
    }

    public List<Estudiante> listarEstudiantes() {
        return estudiantes;
    }

    // Carrera methods
    public void agregarCarrera(Carrera carrera) {
        carreras.add(carrera);
    }

    public List<Carrera> listarCarreras() {
        return carreras;
    }

    // Materia methods
    public void agregarMateria(Materia materia) {
        materias.add(materia);
    }

    public List<Materia> listarMaterias() {
        return materias;
    }
}