package com.example.application.views;

import com.example.application.controllers.SistemaGestionEstudiantes;
import com.example.application.models.carrera.Carrera;
import com.example.application.models.carrera.Materia;
import com.example.application.models.persona.Estudiante;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Inscripciones")
@Route("inscripciones")
@Menu(order = 3, icon = LineAwesomeIconUrl.CARET_SQUARE_DOWN_SOLID)
@Uses(Button.class)
public class InscripcionesView extends Composite<VerticalLayout> {

    private final Grid<Materia> materiasDisponiblesGrid = new Grid<>(Materia.class, false);
    private final Grid<Materia> materiasInscritasGrid = new Grid<>(Materia.class, false);

    @Autowired
    public InscripcionesView(SistemaGestionEstudiantes sistema) {
        FormLayout form = new FormLayout();
        ComboBox<Carrera> carreraCombo = new ComboBox<>("Carrera");
        ComboBox<Estudiante> estudianteCombo = new ComboBox<>("Estudiante");
        Button inscribirBtn = new Button("Inscribir seleccionadas");

        carreraCombo.setItems(sistema.listarCarreras());
        carreraCombo.setItemLabelGenerator(Carrera::getNombre);

        estudianteCombo.setItems(sistema.listarEstudiantes());
        estudianteCombo.setItemLabelGenerator(e -> e.getNombre() + " " + e.getApellido());

        // Grids setup
        materiasDisponiblesGrid.addColumn(Materia::getNombre).setHeader("Materia");
        materiasDisponiblesGrid.addColumn(Materia::getDocente).setHeader("Docente");
        materiasDisponiblesGrid.addColumn(Materia::getHoras).setHeader("Horas");
        materiasDisponiblesGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        materiasDisponiblesGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        materiasInscritasGrid.addColumn(Materia::getNombre).setHeader("Materia");
        materiasInscritasGrid.addColumn(Materia::getDocente).setHeader("Docente");
        materiasInscritasGrid.addColumn(Materia::getHoras).setHeader("Horas");
        materiasInscritasGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        // Update grids when career or student changes
        carreraCombo.addValueChangeListener(event -> actualizarGrids(carreraCombo, estudianteCombo));
        estudianteCombo.addValueChangeListener(event -> actualizarGrids(carreraCombo, estudianteCombo));

        // Inscribir button logic
        inscribirBtn.addClickListener(e -> {
            Estudiante estudiante = estudianteCombo.getValue();
            Carrera carrera = carreraCombo.getValue();
            if (estudiante == null || carrera == null) {
                Notification.show("Seleccione carrera y estudiante");
                return;
            }
            List<Materia> seleccionadas = new ArrayList<>(materiasDisponiblesGrid.getSelectedItems());
            if (seleccionadas.isEmpty()) {
                Notification.show("Seleccione al menos una materia");
                return;
            }
            List<Materia> yaInscritas = estudiante.getMateriasInscritas() != null ? estudiante.getMateriasInscritas() : new ArrayList<>();
            List<Materia> nuevas = seleccionadas.stream()
                    .filter(m -> !yaInscritas.contains(m))
                    .collect(Collectors.toList());
            if (nuevas.isEmpty()) {
                Notification.show("El estudiante ya está inscrito en las materias seleccionadas");
                return;
            }
            yaInscritas.addAll(nuevas);
            estudiante.setMateriasInscritas(yaInscritas);
            Notification.show("Materias inscritas correctamente");
            actualizarGrids(carreraCombo, estudianteCombo);
        });

        form.add(carreraCombo, estudianteCombo);
        HorizontalLayout gridsLayout = new HorizontalLayout();
        gridsLayout.setWidthFull();
        materiasDisponiblesGrid.setWidth("50%");
        materiasInscritasGrid.setWidth("50%");
        gridsLayout.add(materiasDisponiblesGrid, materiasInscritasGrid);

        getContent().add(form, gridsLayout, inscribirBtn);
    }

    private void actualizarGrids(ComboBox<Carrera> carreraCombo, ComboBox<Estudiante> estudianteCombo) {
        Carrera carrera = carreraCombo.getValue();
        Estudiante estudiante = estudianteCombo.getValue();
        List<Materia> inscritas = estudiante != null && estudiante.getMateriasInscritas() != null
                ? estudiante.getMateriasInscritas() : new ArrayList<>();
        if (carrera != null) {
            List<Materia> disponibles = carrera.getMaterias() != null
                    ? carrera.getMaterias().stream()
                    .filter(m -> !inscritas.contains(m))
                    .collect(Collectors.toList())
                    : new ArrayList<>();
            materiasDisponiblesGrid.setItems(disponibles);
        } else {
            materiasDisponiblesGrid.setItems(new ArrayList<>());
        }
        materiasInscritasGrid.setItems(inscritas);
    }
}