package com.example.application.views;


import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import java.util.ArrayList;
import java.util.List;

import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Carreras")
@Route("my-view")
@Menu(order = 2, icon = LineAwesomeIconUrl.USER_GRADUATE_SOLID)
@Uses(Icon.class)
public class CarrerasView extends Composite<VerticalLayout> {

    public CarrerasView() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        FormLayout formLayout3Col = new FormLayout();
        TextField textField = new TextField();
        TextField textField2 = new TextField();
        ComboBox comboBox = new ComboBox();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        H4 h4 = new H4();
        Grid multiSelectGrid = new Grid();
        VerticalLayout layoutColumn3 = new VerticalLayout();
        Button buttonSecondary = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        formLayout3Col.setWidth("100%");
        formLayout3Col.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("250px", 2),
                new ResponsiveStep("500px", 3));
        textField.setLabel("Nombre de la Carrera");
        textField.setWidth("min-content");
        textField2.setLabel("Número de materias");
        textField2.setWidth("min-content");
        comboBox.setLabel("Tipo de Carrera");
        comboBox.setWidth("min-content");
        setComboBoxSampleData(comboBox);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        h4.setText("Materias");
        h4.setWidth("max-content");
        multiSelectGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        multiSelectGrid.setWidth("100%");
        multiSelectGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(multiSelectGrid);
        layoutColumn3.setWidthFull();
        getContent().setFlexGrow(1.0, layoutColumn3);
        layoutColumn3.setWidth("100%");
        layoutColumn3.getStyle().set("flex-grow", "1");
        buttonSecondary.setText("Crear Nueva Carrera");
        layoutColumn3.setAlignSelf(FlexComponent.Alignment.CENTER, buttonSecondary);
        buttonSecondary.setWidth("960px");
        getContent().add(layoutRow);
        layoutRow.add(formLayout3Col);
        formLayout3Col.add(textField);
        formLayout3Col.add(textField2);
        formLayout3Col.add(comboBox);
        getContent().add(layoutColumn2);
        layoutColumn2.add(h4);
        layoutColumn2.add(multiSelectGrid);
        getContent().add(layoutColumn3);
        layoutColumn3.add(buttonSecondary);
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
