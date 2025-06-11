// src/main/java/com/example/application/views/MateriasView.java
package com.example.application.views;

import com.example.application.controllers.SistemaGestionEstudiantes;
import com.example.application.models.carrera.Carrera;
import com.example.application.models.carrera.Materia;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Materias")
@Route("my-view2")
@Menu(order = 1, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
@Uses(Icon.class)
public class MateriasView extends Composite<VerticalLayout> {

    private final Grid<Materia> grid = new Grid<>(Materia.class, false);
    private Materia materiaEditando = null;

    @Autowired
    public MateriasView(SistemaGestionEstudiantes sistema) {
        HorizontalLayout layoutRow = new HorizontalLayout();
        FormLayout formLayout3Col = new FormLayout();
        TextField nombreField = new TextField("Nombre de la materia");
        TextField docenteField = new TextField("Docente de la materia");
        TextField horasField = new TextField("Numero de horas de la materia");
        ComboBox<Carrera> carreraComboBox = new ComboBox<>("Carrera a la que pertenece");
        VerticalLayout layoutColumn2 = new VerticalLayout();
        Button buttonPrimary = new Button("Guardar Materia");

        carreraComboBox.setItems(sistema.listarCarreras());
        carreraComboBox.setItemLabelGenerator(Carrera::getNombre);

        // Grid columns
        grid.addColumn(Materia::getNombre).setHeader("Nombre");
        grid.addColumn(Materia::getDocente).setHeader("Docente");
        grid.addColumn(Materia::getHoras).setHeader("Horas");
        grid.addColumn(materia -> {
            return sistema.listarCarreras().stream()
                    .filter(carrera -> carrera.getMaterias() != null && carrera.getMaterias().contains(materia))
                    .map(Carrera::getNombre)
                    .findFirst()
                    .orElse("Sin carrera");
        }).setHeader("Carrera");
        grid.addComponentColumn(materia -> {
            Button editBtn = new Button("Editar", e -> {
                materiaEditando = materia;
                nombreField.setValue(materia.getNombre());
                docenteField.setValue(materia.getDocente());
                horasField.setValue(String.valueOf(materia.getHoras()));
                Carrera carrera = sistema.listarCarreras().stream()
                        .filter(c -> c.getMaterias() != null && c.getMaterias().contains(materia))
                        .findFirst().orElse(null);
                carreraComboBox.setValue(carrera);
            });
            Button deleteBtn = new Button("Eliminar", e -> {
                Dialog confirmDialog = new Dialog();
                confirmDialog.add("¿Está seguro de eliminar la materia?");
                Button yesBtn = new Button("Sí", ev -> {
                    sistema.listarMaterias().remove(materia);
                    sistema.listarCarreras().forEach(c -> c.getMaterias().remove(materia));
                    actualizarGrid(sistema);
                    confirmDialog.close();
                });
                Button noBtn = new Button("No", ev -> confirmDialog.close());
                HorizontalLayout dialogBtns = new HorizontalLayout(yesBtn, noBtn);
                confirmDialog.add(dialogBtns);
                confirmDialog.open();
            });
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            return new HorizontalLayout(editBtn, deleteBtn);
        }).setHeader("Acciones");
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        grid.setWidth("100%");
        grid.getStyle().set("flex-grow", "0");
        actualizarGrid(sistema);

        // Filtros
        HorizontalLayout filtros = new HorizontalLayout();
        ComboBox<Carrera> filtroCarrera = new ComboBox<>("Filtrar por carrera");
        filtroCarrera.setItems(sistema.listarCarreras());
        filtroCarrera.setItemLabelGenerator(Carrera::getNombre);
        TextField filtroDocente = new TextField("Filtrar por docente");
        filtroDocente.setValueChangeMode(ValueChangeMode.EAGER);

        filtroCarrera.addValueChangeListener(e -> aplicarFiltros(sistema, filtroCarrera, filtroDocente));
        filtroDocente.addValueChangeListener(e -> aplicarFiltros(sistema, filtroCarrera, filtroDocente));
        filtros.add(filtroCarrera, filtroDocente);

        // Add/Edit Materia
        buttonPrimary.addClickListener(e -> {
            if (validarFormulario(nombreField, docenteField, horasField, carreraComboBox)) {
                String nombre = nombreField.getValue();
                String docente = docenteField.getValue();
                int horas = Integer.parseInt(horasField.getValue());
                Carrera carrera = carreraComboBox.getValue();

                if (materiaEditando == null) {
                    // Nueva materia
                    Materia materia = new Materia(nombre, docente, horas);
                    sistema.agregarMateria(materia);
                    carrera.getMaterias().add(materia);
                } else {
                    // Editar materia existente
                    materiaEditando.setNombre(nombre);
                    materiaEditando.setDocente(docente);
                    materiaEditando.setHoras(horas);
                    // Remover de otras carreras y agregar a la seleccionada
                    sistema.listarCarreras().forEach(c -> c.getMaterias().remove(materiaEditando));
                    carrera.getMaterias().add(materiaEditando);
                    materiaEditando = null;
                }
                actualizarGrid(sistema);
                nombreField.clear();
                docenteField.clear();
                horasField.clear();
                carreraComboBox.clear();
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
        formLayout3Col.add(nombreField, docenteField, horasField, carreraComboBox);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");

        getContent().add(layoutRow);
        layoutRow.add(formLayout3Col);
        getContent().add(layoutColumn2);
        layoutColumn2.add(filtros, grid, buttonPrimary);
    }

    private void actualizarGrid(SistemaGestionEstudiantes sistema) {
        grid.setItems(sistema.listarMaterias());
    }

    private void aplicarFiltros(SistemaGestionEstudiantes sistema, ComboBox<Carrera> filtroCarrera, TextField filtroDocente) {
        List<Materia> materias = sistema.listarMaterias();
        Carrera carrera = filtroCarrera.getValue();
        String docente = filtroDocente.getValue() != null ? filtroDocente.getValue().toLowerCase() : "";
        List<Materia> materiasFiltradas = materias.stream()
                .filter(m -> carrera == null || sistema.listarCarreras().stream()
                        .filter(c -> c.equals(carrera))
                        .anyMatch(c -> c.getMaterias() != null && c.getMaterias().contains(m)))
                .filter(m -> docente.isEmpty() || m.getDocente().toLowerCase().contains(docente))
                .collect(Collectors.toList());
        grid.setItems(materiasFiltradas);
    }

    private boolean validarFormulario(TextField nombre, TextField docente, TextField horas, ComboBox<Carrera> carrera) {
        if (nombre.isEmpty() || docente.isEmpty() || horas.isEmpty() || carrera.isEmpty()) {
            Notification.show("Todos los campos son obligatorios");
            return false;
        }
        try {
            Integer.parseInt(horas.getValue());
        } catch (NumberFormatException ex) {
            horas.setInvalid(true);
            horas.setErrorMessage("Debe ser un número");
            Notification.show("El campo horas debe ser numérico");
            return false;
        }
        return true;
    }
}