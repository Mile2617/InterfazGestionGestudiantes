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
import com.vaadin.flow.component.html.Span;
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
    private final Span promedioAcumuladoLabel = new Span("Promedio acumulado: 0.00");

    @Autowired
    public RegistroyConsultadeNotasView(SistemaGestionEstudiantes sistema) {
        FormLayout form = new FormLayout();
        ComboBox<String> periodoCombo = new ComboBox<>("Periodo");
        ComboBox<String> tipoCombo = new ComboBox<>("Tipo de estudio");
        ComboBox<Carrera> carreraCombo = new ComboBox<>("Carrera");
        ComboBox<Estudiante> estudianteCombo = new ComboBox<>("Estudiante");
        Button buscarBtn = new Button("Buscar");

        periodoCombo.setItems("2024-1", "2024-2", "2025-1");
        tipoCombo.setItems("Pregrado", "Postgrado", "Doctorado");

        carreraCombo.setItems(sistema.listarCarreras());
        carreraCombo.setItemLabelGenerator(Carrera::getNombre);

        estudianteCombo.setItems(sistema.listarEstudiantes());
        estudianteCombo.setItemLabelGenerator(e -> e.getNombre() + " " + e.getApellido());

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
            prog1.addValueChangeListener(e -> {
                setNota(materia, 0, e.getValue());
                actualizarPromedioAcumulado();
            });
            return prog1;
        }).setHeader("Progreso 1 (25%)");

        materiasGrid.addComponentColumn(materia -> {
            NumberField prog2 = new NumberField();
            prog2.setWidth("70px");
            prog2.setMin(0);
            prog2.setMax(10);
            prog2.setStep(0.1);
            prog2.setValue(getNota(materia, 1));
            prog2.addValueChangeListener(e -> {
                setNota(materia, 1, e.getValue());
                actualizarPromedioAcumulado();
            });
            return prog2;
        }).setHeader("Progreso 2 (35%)");

        materiasGrid.addComponentColumn(materia -> {
            NumberField prog3 = new NumberField();
            prog3.setWidth("70px");
            prog3.setMin(0);
            prog3.setMax(10);
            prog3.setStep(0.1);
            prog3.setValue(getNota(materia, 2));
            prog3.addValueChangeListener(e -> {
                setNota(materia, 2, e.getValue());
                actualizarPromedioAcumulado();
            });
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
                    actualizarPromedioAcumulado();
                } else {
                    Notification.show("Las notas deben ser numéricas entre 0 y 10");
                }
            });
            return guardarBtn;
        }).setHeader("Acción");

        materiasGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);

        buscarBtn.addClickListener(e -> {
            Estudiante estudiante = estudianteCombo.getValue();
            if (estudiante != null) {
                List<Materia> materias = estudiante.getMateriasInscritas();
                materiasGrid.setItems(materias != null ? materias : new ArrayList<>());
                actualizarPromedioAcumulado();
            } else {
                materiasGrid.setItems(new ArrayList<>());
                promedioAcumuladoLabel.setText("Promedio acumulado: 0.00");
            }
        });

        form.add(periodoCombo, tipoCombo, carreraCombo, estudianteCombo, buscarBtn);
        getContent().add(form, promedioAcumuladoLabel, new H4("Materias inscritas"), materiasGrid);
    }

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

    private void actualizarPromedioAcumulado() {
        List<Materia> materias = new ArrayList<>(notasMateria.keySet());
        double totalHoras = 0;
        double sumaPonderada = 0;

        for (Materia materia : materias) {
            int horas = materia.getHoras();
            double notaFinal = calcularNotaFinal(materia);
            sumaPonderada += notaFinal * horas;
            totalHoras += horas;
        }

        double promedio = totalHoras > 0 ? sumaPonderada / totalHoras : 0;
        promedioAcumuladoLabel.setText("Promedio acumulado: " + String.format("%.2f", promedio));
    }
}