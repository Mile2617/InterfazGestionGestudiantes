package com.example.application.views;

import com.example.application.controllers.SistemaGestionEstudiantes;
import com.example.application.models.persona.Estudiante;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Estudiantes")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.ADDRESS_CARD_SOLID)
@Uses(Icon.class)
public class EstudiantesView extends Composite<VerticalLayout> {

    private final Grid<Estudiante> stripedGrid = new Grid<>(Estudiante.class, false);

    @Autowired
    public EstudiantesView(SistemaGestionEstudiantes sistema) {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        FormLayout formLayout3Col = new FormLayout();
        TextField nombreField = new TextField("Nombre");
        TextField apellidoField = new TextField("Apellido");
        TextField telefonoField = new TextField("Número Telefónico");
        FormLayout formLayout2Col = new FormLayout();
        TextField emailField = new TextField("e-mail");
        ComboBox<String> nivelComboBox = new ComboBox<>("Nivel de estudio");
        Button buttonPrimary = new Button("Crear Nuevo estudiante");

        // Set up ComboBox data
        nivelComboBox.setItems("Pregrado", "Postgrado", "Doctorado");

        // Set up Grid columns
        stripedGrid.addColumn(Estudiante::getNombre).setHeader("Nombre");
        stripedGrid.addColumn(Estudiante::getApellido).setHeader("Apellido");
        stripedGrid.addColumn(Estudiante::getEmail).setHeader("Email");
        stripedGrid.addColumn(Estudiante::getTelefono).setHeader("Teléfono");
        stripedGrid.addColumn(Estudiante::getNivelEstudio).setHeader("Nivel de estudio");
        stripedGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        stripedGrid.setWidth("100%");
        stripedGrid.getStyle().set("flex-grow", "0");
        stripedGrid.setItems(sistema.listarEstudiantes());

        // Button action
        buttonPrimary.addClickListener(e -> {
            String nombre = nombreField.getValue();
            String apellido = apellidoField.getValue();
            String email = emailField.getValue();
            String telefono = telefonoField.getValue();
            String nivel = nivelComboBox.getValue();
            if (!nombre.isEmpty() && !apellido.isEmpty() && !email.isEmpty() && !telefono.isEmpty() && nivel != null) {
                Estudiante estudiante = new Estudiante(
                        nombre, apellido, email, telefono, "", "", nivel, ""
                );
                sistema.agregarEstudiante(estudiante);
                stripedGrid.setItems(sistema.listarEstudiantes());
                // Clear fields
                nombreField.clear();
                apellidoField.clear();
                emailField.clear();
                telefonoField.clear();
                nivelComboBox.clear();
            }
        });

        // Layout setup
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.addClassName(Padding.SMALL);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        formLayout3Col.setWidth("100%");
        formLayout3Col.setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("250px", 2),
                new ResponsiveStep("500px", 3)
        );
        formLayout3Col.add(nombreField, apellidoField, telefonoField);
        formLayout2Col.setWidth("100%");
        formLayout2Col.add(emailField, nivelComboBox);
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, buttonPrimary);
        buttonPrimary.setWidth("970px");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(layoutColumn2);
        layoutColumn2.add(formLayout3Col, formLayout2Col, buttonPrimary, stripedGrid);
    }
}