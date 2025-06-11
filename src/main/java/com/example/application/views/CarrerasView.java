// src/main/java/com/example/application/views/CarrerasView.java
package com.example.application.views;

import com.example.application.controllers.SistemaGestionEstudiantes;
import com.example.application.models.carrera.Carrera;
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
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.ArrayList;

@PageTitle("Carreras")
@Route("my-view")
@Menu(order = 0, icon = LineAwesomeIconUrl.USER_GRADUATE_SOLID)
@Uses(Icon.class)
public class CarrerasView extends Composite<VerticalLayout> {

    private final Grid<Carrera> grid = new Grid<>(Carrera.class, false);
    private Carrera carreraEditando = null;

    @Autowired
    public CarrerasView(SistemaGestionEstudiantes sistema) {
        HorizontalLayout layoutRow = new HorizontalLayout();
        FormLayout formLayout3Col = new FormLayout();
        TextField nombreField = new TextField("Nombre de la Carrera");
        TextField numMateriasField = new TextField("Número de materias");
        ComboBox<String> tipoComboBox = new ComboBox<>("Tipo de Carrera");
        TextField duracionField = new TextField("Duración (semestres)");
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H4 h4 = new H4("Materias");
        Button buttonSecondary = new Button("Guardar Carrera");

        tipoComboBox.setItems("Pregrado", "Postgrado", "Doctorado");

        // Grid columns
        grid.addColumn(Carrera::getNombre).setHeader("Nombre");
        grid.addColumn(Carrera::getTipo).setHeader("Tipo");
        grid.addColumn(c -> c.getMaterias() != null ? c.getMaterias().size() : 0)
                .setHeader("Número de Materias");
        grid.addColumn(Carrera::getDuracion).setHeader("Duración");
        grid.addComponentColumn(carrera -> {
            Button editBtn = new Button("Editar", e -> {
                carreraEditando = carrera;
                nombreField.setValue(carrera.getNombre());
                tipoComboBox.setValue(carrera.getTipo());
                duracionField.setValue(String.valueOf(carrera.getDuracion()));
                numMateriasField.setValue(String.valueOf(carrera.getMaterias() != null ? carrera.getMaterias().size() : 0));
            });
            Button deleteBtn = new Button("Eliminar", e -> {
                Dialog confirmDialog = new Dialog();
                confirmDialog.add("¿Está seguro de eliminar la carrera?");
                Button yesBtn = new Button("Sí", ev -> {
                    sistema.listarCarreras().remove(carrera);
                    grid.setItems(sistema.listarCarreras());
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
        grid.setItems(sistema.listarCarreras());

        // Button action
        buttonSecondary.addClickListener(e -> {
            String nombre = nombreField.getValue();
            String tipo = tipoComboBox.getValue();
            String duracionStr = duracionField.getValue();
            int duracion = 0;
            try {
                if (duracionStr != null && !duracionStr.isEmpty()) {
                    duracion = Integer.parseInt(duracionStr);
                }
            } catch (NumberFormatException ex) {
                duracionField.setInvalid(true);
                duracionField.setErrorMessage("Debe ser un número");
                Notification.show("Duración debe ser un número");
                return;
            }
            // Validations
            if (nombre.isEmpty() || tipo == null || duracionStr.isEmpty()) {
                Notification.show("Todos los campos son obligatorios");
                return;
            }
            if (carreraEditando == null) {
                Carrera carrera = new Carrera(nombre, tipo, new ArrayList<>(), duracion);
                sistema.agregarCarrera(carrera);
            } else {
                carreraEditando.setNombre(nombre);
                carreraEditando.setTipo(tipo);
                carreraEditando.setDuracion(duracion);
                carreraEditando = null;
            }
            grid.setItems(sistema.listarCarreras());
            nombreField.clear();
            numMateriasField.clear();
            tipoComboBox.clear();
            duracionField.clear();
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
        formLayout3Col.add(nombreField, numMateriasField, tipoComboBox, duracionField);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        h4.setWidth("max-content");

        getContent().add(layoutRow);
        layoutRow.add(formLayout3Col);
        getContent().add(layoutColumn2);
        layoutColumn2.add(h4, grid, buttonSecondary);
    }
}