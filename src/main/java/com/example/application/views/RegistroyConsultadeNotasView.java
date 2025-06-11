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
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

import java.util.*;

@PageTitle("Registro y Consulta de Notas")
@Route("registro-consulta")
@Menu(order = 4, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
@Uses(Button.class)
public class RegistroyConsultadeNotasView extends Composite<VerticalLayout> {

    private final Grid<Materia> materiasGrid = new Grid<>(Materia.class, false);
    private final Map<Materia, double[]> notasMateria = new HashMap<>();
    private Carrera carreraSeleccionada = null;
    private Estudiante estudianteSeleccionado = null;

    @Autowired
    public RegistroyConsultadeNotasView(SistemaGestionEstudiantes sistema) {
        FormLayout form = new FormLayout();
        ComboBox<String> modoBusquedaCombo = new ComboBox<>("Buscar por");
        ComboBox<Object> selectorCombo = new ComboBox<>();

        modoBusquedaCombo.setItems("Estudiante", "Carrera");
        modoBusquedaCombo.setValue("Estudiante");
        selectorCombo.setLabel("Seleccione");

        // Grid columns
        materiasGrid.addColumn(Materia::getNombre).setHeader("Materia");
        materiasGrid.addColumn(Materia::getDocente).setHeader("Docente");
        materiasGrid.addColumn(Materia::getHoras).setHeader("Horas");

        materiasGrid.addComponentColumn(materia -> {
            NumberField prog1 = new NumberField();
            prog1.setWidth("70px");
            prog1.setMin(0);
            prog1.setMax(10);
            prog1.setStep(0.1);
            prog1.setValue(getNota(materia, 0));
            prog1.addValueChangeListener(e -> setNota(materia, 0, e.getValue()));
            return prog1;
        }).setHeader("Progreso 1 (25%)");

        materiasGrid.addComponentColumn(materia -> {
            NumberField prog2 = new NumberField();
            prog2.setWidth("70px");
            prog2.setMin(0);
            prog2.setMax(10);
            prog2.setStep(0.1);
            prog2.setValue(getNota(materia, 1));
            prog2.addValueChangeListener(e -> setNota(materia, 1, e.getValue()));
            return prog2;
        }).setHeader("Progreso 2 (35%)");

        materiasGrid.addComponentColumn(materia -> {
            NumberField prog3 = new NumberField();
            prog3.setWidth("70px");
            prog3.setMin(0);
            prog3.setMax(10);
            prog3.setStep(0.1);
            prog3.setValue(getNota(materia, 2));
            prog3.addValueChangeListener(e -> setNota(materia, 2, e.getValue()));
            return prog3;
        }).setHeader("Progreso 3 (40%)");

        materiasGrid.addColumn(materia -> {
            double notaFinal = calcularNotaFinal(materia);
            return String.format("%.2f", notaFinal);
        }).setHeader("Nota Final");

        materiasGrid.addComponentColumn(materia -> {
            Button guardarBtn = new Button("Guardar notas", e -> {
                if (validarNotas(materia)) {
                    Notification.show("Notas guardadas. Nota final: " +
                            String.format("%.2f", calcularNotaFinal(materia)));
                    materiasGrid.getDataProvider().refreshItem(materia);
                } else {
                    Notification.show("Las notas deben ser numéricas entre 0 y 10");
                }
            });
            return guardarBtn;
        }).setHeader("Acción");

        materiasGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        // Search mode logic
        modoBusquedaCombo.addValueChangeListener(e -> {
            String modo = modoBusquedaCombo.getValue();
            selectorCombo.clear();
            materiasGrid.setItems(new ArrayList<>());
            estudianteSeleccionado = null;
            carreraSeleccionada = null;

            if ("Estudiante".equals(modo)) {
                selectorCombo.setItems(sistema.listarEstudiantes());
                selectorCombo.setItemLabelGenerator(obj -> {
                    Estudiante est = (Estudiante) obj;
                    return est.getNombre() + " " + est.getApellido();
                });
                selectorCombo.setLabel("Estudiante");
            } else {
                selectorCombo.setItems(sistema.listarCarreras());
                selectorCombo.setItemLabelGenerator(obj -> {
                    Carrera c = (Carrera) obj;
                    return c.getNombre();
                });
                selectorCombo.setLabel("Carrera");
            }
        });

        selectorCombo.addValueChangeListener(e -> {
            if ("Estudiante".equals(modoBusquedaCombo.getValue())) {
                estudianteSeleccionado = (Estudiante) selectorCombo.getValue();
                carreraSeleccionada = null;
                actualizarGridPorEstudiante();
            } else {
                carreraSeleccionada = (Carrera) selectorCombo.getValue();
                estudianteSeleccionado = null;
                actualizarGridPorCarrera();
            }
        });

        // Initial setup
        modoBusquedaCombo.setValue("Estudiante");
        selectorCombo.setItems(sistema.listarEstudiantes());
        selectorCombo.setItemLabelGenerator(obj -> {
            Estudiante est = (Estudiante) obj;
            return est.getNombre() + " " + est.getApellido();
        });
        selectorCombo.setLabel("Estudiante");

        form.add(modoBusquedaCombo, selectorCombo);
        getContent().add(form, new H4("Materias inscritas"), materiasGrid);
    }

    private void actualizarGridPorEstudiante() {
        if (estudianteSeleccionado == null) {
            materiasGrid.setItems(new ArrayList<>());
            return;
        }
        List<Materia> materias = estudianteSeleccionado.getMateriasInscritas();
        materiasGrid.setItems(materias != null ? materias : new ArrayList<>());
    }

    private void actualizarGridPorCarrera() {
        if (carreraSeleccionada == null) {
            materiasGrid.setItems(new ArrayList<>());
            return;
        }
        List<Materia> materias = carreraSeleccionada.getMaterias();
        materiasGrid.setItems(materias != null ? materias : new ArrayList<>());
    }

    // Helpers for storing and retrieving grades
    private double getNota(Materia materia, int idx) {
        notasMateria.putIfAbsent(materia, new double[3]);
        return notasMateria.get(materia)[idx];
    }

    private void setNota(Materia materia, int idx, Double valor) {
        if (materia == null || valor == null || valor < 0 || valor > 10) return;
        notasMateria.putIfAbsent(materia, new double[3]);
        notasMateria.get(materia)[idx] = valor;
    }

    private boolean validarNotas(Materia materia) {
        double[] notas = notasMateria.getOrDefault(materia, new double[3]);
        for (double n : notas) {
            if (n < 0 || n > 10) return false;
        }
        return true;
    }

    private double calcularNotaFinal(Materia materia) {
        double[] notas = notasMateria.getOrDefault(materia, new double[3]);
        return notas[0] * 0.25 + notas[1] * 0.35 + notas[2] * 0.40;
    }
}