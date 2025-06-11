package com.example.application.views;

import com.example.application.controllers.SistemaGestionEstudiantes;
import com.example.application.models.carrera.Carrera;
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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PageTitle("Estudiantes")
@Route("")
@Menu(order = 2, icon = LineAwesomeIconUrl.ADDRESS_CARD_SOLID)
@Uses(Icon.class)
public class EstudiantesView extends Composite<VerticalLayout> {

    private final Grid<Estudiante> stripedGrid = new Grid<>(Estudiante.class, false);
    private List<Estudiante> estudiantesFiltrados = new ArrayList<>();

    @Autowired
    public EstudiantesView(SistemaGestionEstudiantes sistema) {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        FormLayout formLayout3Col = new FormLayout();
        TextField nombreField = new TextField("Nombre");
        TextField apellidoField = new TextField("Apellido");
        TextField telefonoField = new TextField("Número Telefónico");
        EmailField emailField = new EmailField("e-mail");
        ComboBox<String> nivelComboBox = new ComboBox<>("Nivel de estudio");
        ComboBox<String> estadoComboBox = new ComboBox<>("Estado");
        ComboBox<Carrera> carreraComboBox = new ComboBox<>("Carrera");
        Button buttonPrimary = new Button("Crear Nuevo estudiante");

        // Set up ComboBox data
        nivelComboBox.setItems("Pregrado", "Postgrado", "Doctorado");
        estadoComboBox.setItems("Activo", "Retirado", "Egresado");
        carreraComboBox.setItems(sistema.listarCarreras());
        carreraComboBox.setItemLabelGenerator(Carrera::getNombre);

        // Form validation
        nombreField.setRequired(true);
        apellidoField.setRequired(true);
        emailField.setRequiredIndicatorVisible(true);
        telefonoField.setRequired(true);
        nivelComboBox.setRequired(true);
        estadoComboBox.setRequired(true);
        carreraComboBox.setRequired(true);

        // Set up Grid columns
        stripedGrid.addColumn(Estudiante::getNombre).setHeader("Nombre");
        stripedGrid.addColumn(Estudiante::getApellido).setHeader("Apellido");
        stripedGrid.addColumn(Estudiante::getEmail).setHeader("Email");
        stripedGrid.addColumn(Estudiante::getTelefono).setHeader("Teléfono");
        stripedGrid.addColumn(Estudiante::getNivelEstudio).setHeader("Nivel de estudio");
        stripedGrid.addColumn(est -> est.getEstado() != null ? est.getEstado() : "").setHeader("Estado");
        stripedGrid.addColumn(est -> est.getCarrera() != null ? est.getCarrera().getNombre() : "").setHeader("Carrera");
        stripedGrid.addComponentColumn(est -> {
            Button editBtn = new Button("Editar", e -> editarEstudiante(est, nombreField, apellidoField, emailField, telefonoField, nivelComboBox, estadoComboBox, carreraComboBox));
            Button deleteBtn = new Button("Eliminar", e -> {
                sistema.listarEstudiantes().remove(est);
                actualizarGrid(sistema);
            });
            editBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
            HorizontalLayout actions = new HorizontalLayout(editBtn, deleteBtn);
            return actions;
        }).setHeader("Acciones");
        stripedGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        stripedGrid.setWidth("100%");
        stripedGrid.getStyle().set("flex-grow", "0");

        // Filtros
        HorizontalLayout filtros = new HorizontalLayout();
        TextField filtroNombre = new TextField("Filtrar por nombre");
        ComboBox<String> filtroNivel = new ComboBox<>("Filtrar por nivel");
        ComboBox<Carrera> filtroCarrera = new ComboBox<>("Filtrar por carrera");
        filtroNivel.setItems("Pregrado", "Postgrado", "Doctorado");
        filtroCarrera.setItems(sistema.listarCarreras());
        filtroCarrera.setItemLabelGenerator(Carrera::getNombre);

        filtroNombre.setValueChangeMode(ValueChangeMode.EAGER);
        filtroNombre.addValueChangeListener(e -> aplicarFiltros(sistema, filtroNombre, filtroNivel, filtroCarrera));
        filtroNivel.addValueChangeListener(e -> aplicarFiltros(sistema, filtroNombre, filtroNivel, filtroCarrera));
        filtroCarrera.addValueChangeListener(e -> aplicarFiltros(sistema, filtroNombre, filtroNivel, filtroCarrera));
        filtros.add(filtroNombre, filtroNivel, filtroCarrera);

        // Inicializar grid
        actualizarGrid(sistema);

        // Button action
        buttonPrimary.addClickListener(e -> {
            if (validarFormulario(nombreField, apellidoField, emailField, telefonoField, nivelComboBox, estadoComboBox, carreraComboBox)) {
                Estudiante estudiante = new Estudiante(
                        nombreField.getValue(),
                        apellidoField.getValue(),
                        emailField.getValue(),
                        telefonoField.getValue(),
                        "", "", nivelComboBox.getValue(), ""
                );
                estudiante.setEstado(estadoComboBox.getValue());
                estudiante.setCarrera(carreraComboBox.getValue());
                sistema.agregarEstudiante(estudiante);
                actualizarGrid(sistema);
                // Clear fields
                nombreField.clear();
                apellidoField.clear();
                emailField.clear();
                telefonoField.clear();
                nivelComboBox.clear();
                estadoComboBox.clear();
                carreraComboBox.clear();
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
        formLayout3Col.add(nombreField, apellidoField, telefonoField, emailField, nivelComboBox, estadoComboBox, carreraComboBox);
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, buttonPrimary);
        buttonPrimary.setWidth("970px");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        getContent().add(layoutColumn2);
        layoutColumn2.add(formLayout3Col, buttonPrimary, filtros, stripedGrid);
    }

    private void actualizarGrid(SistemaGestionEstudiantes sistema) {
        stripedGrid.setItems(sistema.listarEstudiantes());
    }

    private void aplicarFiltros(SistemaGestionEstudiantes sistema, TextField filtroNombre, ComboBox<String> filtroNivel, ComboBox<Carrera> filtroCarrera) {
        List<Estudiante> estudiantes = sistema.listarEstudiantes();
        String nombre = filtroNombre.getValue() != null ? filtroNombre.getValue().toLowerCase() : "";
        String nivel = filtroNivel.getValue();
        Carrera carrera = filtroCarrera.getValue();
        estudiantesFiltrados = estudiantes.stream()
                .filter(e -> nombre.isEmpty() || e.getNombre().toLowerCase().contains(nombre))
                .filter(e -> nivel == null || nivel.equals(e.getNivelEstudio()))
                .filter(e -> carrera == null || (e.getCarrera() != null && carrera.equals(e.getCarrera())))
                .collect(Collectors.toList());
        stripedGrid.setItems(estudiantesFiltrados);
    }

    private boolean validarFormulario(TextField nombre, TextField apellido, EmailField email, TextField telefono,
                                      ComboBox<String> nivel, ComboBox<String> estado, ComboBox<Carrera> carrera) {
        boolean valido = true;
        if (nombre.isEmpty() || apellido.isEmpty() || email.isEmpty() || telefono.isEmpty()
                || nivel.isEmpty() || estado.isEmpty() || carrera.isEmpty()) {
            Notification.show("Todos los campos son obligatorios");
            valido = false;
        } else if (!email.getValue().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            email.setInvalid(true);
            email.setErrorMessage("Formato de email inválido");
            Notification.show("Formato de email inválido");
            valido = false;
        } else if (!telefono.getValue().matches("\\d+")) {
            telefono.setInvalid(true);
            telefono.setErrorMessage("El teléfono debe ser numérico");
            Notification.show("El teléfono debe ser numérico");
            valido = false;
        }
        return valido;
    }

    private void editarEstudiante(Estudiante est, TextField nombre, TextField apellido, EmailField email, TextField telefono,
                                  ComboBox<String> nivel, ComboBox<String> estado, ComboBox<Carrera> carrera) {
        nombre.setValue(est.getNombre());
        apellido.setValue(est.getApellido());
        email.setValue(est.getEmail());
        telefono.setValue(est.getTelefono());
        nivel.setValue(est.getNivelEstudio());
        estado.setValue(est.getEstado());
        carrera.setValue(est.getCarrera());
    }
}