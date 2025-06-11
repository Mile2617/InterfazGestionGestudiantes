package com.example.application.models.persona;

import com.example.application.models.carrera.Materia;
import java.util.ArrayList;
import java.util.List;

public class Estudiante extends Persona {
    private String nivelEstudio, rol;
    private List<Materia> materiasInscritas = new ArrayList<>();

    public Estudiante(String nombre, String apellido, String email, String telefono,
                      String fechaNacimiento, String ocupacion, String nivelEstudio, String rol) {
        super(nombre, apellido, email, telefono, fechaNacimiento, ocupacion);
        this.nivelEstudio = nivelEstudio;
        this.rol = rol;
    }

    @Override
    public String getTipo() {
        return "Estudiante";
    }

    public String getNivelEstudio() {
        return nivelEstudio;
    }

    public void setNivelEstudio(String nivelEstudio) {
        this.nivelEstudio = nivelEstudio;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public List<Materia> getMateriasInscritas() {
        return materiasInscritas;
    }

    public void setMateriasInscritas(List<Materia> materiasInscritas) {
        this.materiasInscritas = materiasInscritas;
    }
}