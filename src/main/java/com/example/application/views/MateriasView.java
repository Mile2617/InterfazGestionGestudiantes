package com.example.application.views;

import com.example.application.controllers.SistemaGestionEstudiantes;
import com.example.application.models.carrera.Materia;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Materias")
@Route("my-view2")
@Menu(order = 1, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
@Uses(Icon.class)
public class MateriasView extends Composite<VerticalLayout> {

    private final Grid<Materia> grid = new Grid<>(Materia.class, false);

    @Autowired
    public MateriasView(SistemaGestionEstudiantes sistema) {
        HorizontalLayout layoutRow = new HorizontalLayout();
        FormLayout formLayout3Col = new FormLayout();
        TextField nombreField = new TextField("Nombre de la materia");
        TextField docenteField = new TextField("Docente de la materia");
        TextField horasField = new TextField("Numero de horas de la materia");
        VerticalLayout layoutColumn2 = new VerticalLayout();
        Button buttonPrimary = new Button("Crear Materia");
        Button buttonSecondary = new Button("Eliminar Materia");

        // Grid columns
        grid.addColumn(Materia::getNombre).setHeader("Nombre");
        grid.addColumn(Materia::getDocente).setHeader("Docente");
        grid.addColumn(Materia::getHoras).setHeader("Horas");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setWidth("100%");
        grid.getStyle().set("flex-grow", "0");
        grid.setItems(sistema.listarMaterias());
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);

        // Add Materia
        buttonPrimary.addClickListener(e -> {
            String nombre = nombreField.getValue();
            String docente = docenteField.getValue();
            String horasStr = horasField.getValue();
            if (!nombre.isEmpty() && !docente.isEmpty() && !horasStr.isEmpty()) {
                try {
                    int horas = Integer.parseInt(horasStr);
                    Materia materia = new Materia(nombre, docente, horas);
                    sistema.agregarMateria(materia);
                    grid.setItems(sistema.listarMaterias());
                    nombreField.clear();
                    docenteField.clear();
                    horasField.clear();
                } catch (NumberFormatException ex) {
                    horasField.setInvalid(true);
                    horasField.setErrorMessage("Debe ser un número");
                }
            }
        });

        // Delete Materia
        buttonSecondary.addClickListener(e -> {
            Materia selected = grid.asSingleSelect().getValue();
            if (selected != null) {
                sistema.listarMaterias().remove(selected);
                grid.setItems(sistema.listarMaterias());
                grid.asSingleSelect().clear();
            }
        });

        // Layout setup
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        formLayout3Col.setWidth("100%");
        formLayout3Col.setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("250px", 2),
                new ResponsiveStep("500px", 3)
        );
        formLayout3Col.add(nombreField, docenteField, horasField);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");

        getContent().add(layoutRow);
        layoutRow.add(formLayout3Col);
        getContent().add(layoutColumn2);
        layoutColumn2.add(grid, buttonPrimary, buttonSecondary);
    }
}