package com.example.application.views;


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

import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Estudiantes")
@Route("")
@Menu(order = 0, icon = LineAwesomeIconUrl.ADDRESS_CARD_SOLID)
@Uses(Icon.class)
public class EstudiantesView extends Composite<VerticalLayout> {

    public EstudiantesView() {
        VerticalLayout layoutColumn2 = new VerticalLayout();
        FormLayout formLayout3Col = new FormLayout();
        TextField textField = new TextField();
        TextField textField2 = new TextField();
        TextField textField3 = new TextField();
        FormLayout formLayout2Col = new FormLayout();
        TextField textField4 = new TextField();
        ComboBox comboBox = new ComboBox();
        Button buttonPrimary = new Button();
        Grid stripedGrid = new Grid();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutColumn2.addClassName(Padding.SMALL);
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        formLayout3Col.setWidth("100%");
        formLayout3Col.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("250px", 2),
                new ResponsiveStep("500px", 3));
        textField.setLabel("Nombre");
        textField.setWidth("min-content");
        textField2.setLabel("Apellido");
        textField2.setWidth("min-content");
        textField3.setLabel("Número Telefónico");
        textField3.setWidth("min-content");
        formLayout2Col.setWidth("100%");
        textField4.setLabel("e-mail");
        textField4.setWidth("min-content");
        comboBox.setLabel("Nivel de estudio");
        comboBox.setWidth("min-content");
        setComboBoxSampleData(comboBox);
        buttonPrimary.setText("Crear Nuevo estudiante");
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, buttonPrimary);
        buttonPrimary.setWidth("970px");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        stripedGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        stripedGrid.setWidth("100%");
        stripedGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(stripedGrid);
        getContent().add(layoutColumn2);
        layoutColumn2.add(formLayout3Col);
        formLayout3Col.add(textField);
        formLayout3Col.add(textField2);
        formLayout3Col.add(textField3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(textField4);
        formLayout2Col.add(comboBox);
        layoutColumn2.add(buttonPrimary);
        layoutColumn2.add(stripedGrid);
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
