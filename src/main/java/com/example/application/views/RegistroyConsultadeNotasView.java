package com.example.application.views;

import com.example.application.controllers.SistemaGestionEstudiantes;
import com.example.application.models.carrera.Carrera;
import com.example.application.models.carrera.Materia;
import com.example.application.models.persona.Estudiante;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Registro y Consulta de Notas")
@Route("registro-consulta")
@Menu(order = 4, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
@Uses(Button.class)
public class RegistroyConsultadeNotasView extends Composite<VerticalLayout> {

    private final Grid<Materia> materiasGrid = new Grid<>(Materia.class, false);

    @Autowired
    public RegistroyConsultadeNotasView(SistemaGestionEstudiantes sistema) {
        FormLayout form = new FormLayout();
        ComboBox<String> periodoCombo = new ComboBox<>("Periodo");
        ComboBox<String> tipoCombo = new ComboBox<>("Tipo de estudio");
        ComboBox<Carrera> carreraCombo = new ComboBox<>("Carrera");
        ComboBox<Estudiante> estudianteCombo = new ComboBox<>("Estudiante");
        Button buscarBtn = new Button("Buscar");

        // Sample periods
        periodoCombo.setItems("2024-1", "2024-2", "2025-1");
        tipoCombo.setItems("Pregrado", "Postgrado", "Doctorado");

        carreraCombo.setItems(sistema.listarCarreras());
        carreraCombo.setItemLabelGenerator(Carrera::getNombre);

        estudianteCombo.setItems(sistema.listarEstudiantes());
        estudianteCombo.setItemLabelGenerator(e -> e.getNombre() + " " + e.getApellido());

        materiasGrid.addColumn(Materia::getNombre).setHeader("Materia");
        materiasGrid.addColumn(Materia::getDocente).setHeader("Docente");
        materiasGrid.addColumn(Materia::getHoras).setHeader("Horas");
        materiasGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        buscarBtn.addClickListener(e -> {
            Estudiante estudiante = estudianteCombo.getValue();
            if (estudiante != null) {
                List<Materia> materias = estudiante.getMateriasInscritas();
                materiasGrid.setItems(materias != null ? materias : new ArrayList<>());
            } else {
                materiasGrid.setItems(new ArrayList<>());
            }
        });

        form.add(periodoCombo, tipoCombo, carreraCombo, estudianteCombo, buscarBtn);
        getContent().add(form, new H4("Materias inscritas"), materiasGrid);
    }
}