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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Inscripciones")
@Route("inscripciones")
@Menu(order = 3, icon = LineAwesomeIconUrl.CARET_SQUARE_DOWN_SOLID)
@Uses(Button.class)
public class InscripcionesView extends Composite<VerticalLayout> {

    private final Grid<Materia> materiasGrid = new Grid<>(Materia.class, false);

    @Autowired
    public InscripcionesView(SistemaGestionEstudiantes sistema) {
        FormLayout form = new FormLayout();
        ComboBox<Carrera> carreraCombo = new ComboBox<>("Carrera");
        ComboBox<Estudiante> estudianteCombo = new ComboBox<>("Estudiante");
        Button inscribirBtn = new Button("Inscribir");

        // Set items and label generators
        carreraCombo.setItems(sistema.listarCarreras());
        carreraCombo.setItemLabelGenerator(Carrera::getNombre);

        estudianteCombo.setItems(sistema.listarEstudiantes());
        estudianteCombo.setItemLabelGenerator(e -> e.getNombre() + " " + e.getApellido());

        // Grid setup
        materiasGrid.addColumn(Materia::getNombre).setHeader("Materia");
        materiasGrid.addColumn(Materia::getDocente).setHeader("Docente");
        materiasGrid.addColumn(Materia::getHoras).setHeader("Horas");
        materiasGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        materiasGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        // Update materias when a career is selected
        carreraCombo.addValueChangeListener(event -> {
            Carrera selected = event.getValue();
            if (selected != null) {
                materiasGrid.setItems(selected.getMaterias());
            } else {
                materiasGrid.setItems(new ArrayList<>());
            }
        });

        // Inscribir button logic (add materias to estudiante)
        inscribirBtn.addClickListener(e -> {
            Estudiante estudiante = estudianteCombo.getValue();
            List<Materia> materiasSeleccionadas = new ArrayList<>(materiasGrid.getSelectedItems());
            if (estudiante != null && !materiasSeleccionadas.isEmpty()) {
                estudiante.setMateriasInscritas(materiasSeleccionadas);
                UI.getCurrent().getPage().reload();
            }
        });

        form.add(carreraCombo, estudianteCombo);
        getContent().add(form, materiasGrid, inscribirBtn);
    }
}