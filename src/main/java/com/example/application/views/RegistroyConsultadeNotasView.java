package com.example.application.views;


import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Registro y Consulta de Notas")
@Route("my-view4")
@Menu(order = 4, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
@Uses(Icon.class)
public class RegistroyConsultadeNotasView extends Composite<VerticalLayout> {

    public RegistroyConsultadeNotasView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        FormLayout formLayout3Col = new FormLayout();
        ComboBox comboBox = new ComboBox();
        ComboBox comboBox2 = new ComboBox();
        FormLayout formLayout2Col = new FormLayout();
        ComboBox comboBox3 = new ComboBox();
        ComboBox comboBox4 = new ComboBox();
        Button buttonPrimary = new Button();
        H4 h4 = new H4();
        VerticalLayout layoutColumn3 = new VerticalLayout();
        Grid multiSelectGrid = new Grid();
        FormLayout formLayout2Col2 = new FormLayout();
        Button buttonPrimary2 = new Button();
        Button buttonSecondary = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn2);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        formLayout3Col.setWidth("100%");
        formLayout3Col.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("250px", 2),
                new ResponsiveStep("500px", 3));
        comboBox.setLabel("Periodo");
        comboBox.setWidth("min-content");
        setComboBoxSampleData(comboBox);
        comboBox2.setLabel("Tipo de estuido");
        comboBox2.setWidth("min-content");
        setComboBoxSampleData(comboBox2);
        formLayout2Col.setWidth("100%");
        comboBox3.setLabel("Carrera");
        comboBox3.setWidth("min-content");
        setComboBoxSampleData(comboBox3);
        comboBox4.setLabel("Estudiante");
        comboBox4.setWidth("min-content");
        setComboBoxSampleData(comboBox4);
        buttonPrimary.setText("Buscar");
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, buttonPrimary);
        buttonPrimary.setWidth("970px");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        h4.setText("Materias inscritas");
        h4.setWidth("max-content");
        layoutColumn3.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutColumn3);
        layoutColumn3.setWidth("100%");
        layoutColumn3.getStyle().set("flex-grow", "1");
        multiSelectGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        multiSelectGrid.setWidth("100%");
        multiSelectGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(multiSelectGrid);
        formLayout2Col2.setWidth("100%");
        buttonPrimary2.setText("Guardar");
        buttonPrimary2.setWidth("min-content");
        buttonPrimary2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSecondary.setText("Cancelar");
        buttonSecondary.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(formLayout3Col);
        formLayout3Col.add(comboBox);
        formLayout3Col.add(comboBox2);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(comboBox3);
        formLayout2Col.add(comboBox4);
        layoutColumn2.add(buttonPrimary);
        layoutColumn2.add(h4);
        layoutColumn2.add(layoutColumn3);
        layoutColumn3.add(multiSelectGrid);
        layoutColumn2.add(formLayout2Col2);
        formLayout2Col2.add(buttonPrimary2);
        formLayout2Col2.add(buttonSecondary);
    }

    record SampleItem(String value, String label, Boolean disabled) {
    }

    private void setComboBoxSampleData(ComboBox comboBox) {
        List<SampleItem> sampleItems = new ArrayList<>();
        sampleItems.add(new SampleItem("first", "First", null));
        sampleItems.add(new SampleItem("second", "Second", null));
        sampleItems.add(new SampleItem("third", "Third", Boolean.TRUE));
        sampleItems.add(new SampleItem("fourth", "Fourth", null));
        comboBox.setItems(sampleItems);
        comboBox.setItemLabelGenerator(item -> ((SampleItem) item).label());
    }

    private void setGridSampleData(Grid grid) {
       // grid.setItems(query -> samplePersonService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
    }


}
