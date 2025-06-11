package com.example.application.views;


import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.formlayout.FormLayout.ResponsiveStep;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import org.vaadin.lineawesome.LineAwesomeIconUrl;

@PageTitle("Materias")
@Route("my-view2")
@Menu(order = 1, icon = LineAwesomeIconUrl.PENCIL_RULER_SOLID)
@Uses(Icon.class)
public class MateriasView extends Composite<VerticalLayout> {

    public MateriasView() {
        HorizontalLayout layoutRow = new HorizontalLayout();
        FormLayout formLayout3Col = new FormLayout();
        TextField textField = new TextField();
        TextField textField2 = new TextField();
        TextField textField3 = new TextField();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        Grid multiSelectGrid = new Grid();
        VerticalLayout layoutColumn3 = new VerticalLayout();
        FormLayout formLayout2Col = new FormLayout();
        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        formLayout3Col.setWidth("100%");
        formLayout3Col.setResponsiveSteps(new ResponsiveStep("0", 1), new ResponsiveStep("250px", 2),
                new ResponsiveStep("500px", 3));
        textField.setLabel("Nombre de la materia");
        textField.setWidth("min-content");
        textField2.setLabel("Docente de la materia");
        textField2.setWidth("min-content");
        textField3.setLabel("Numero de horas de la materia");
        textField3.setWidth("min-content");
        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        multiSelectGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        multiSelectGrid.setWidth("100%");
        multiSelectGrid.getStyle().set("flex-grow", "0");
        setGridSampleData(multiSelectGrid);
        layoutColumn3.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutColumn3);
        layoutColumn3.setWidth("100%");
        layoutColumn3.getStyle().set("flex-grow", "1");
        formLayout2Col.setWidth("100%");
        buttonPrimary.setText("Crear Materia");
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonSecondary.setText("Eliminar Materia");
        buttonSecondary.setWidth("min-content");
        getContent().add(layoutRow);
        layoutRow.add(formLayout3Col);
        formLayout3Col.add(textField);
        formLayout3Col.add(textField2);
        formLayout3Col.add(textField3);
        getContent().add(layoutColumn2);
        layoutColumn2.add(multiSelectGrid);
        layoutColumn2.add(layoutColumn3);
        layoutColumn3.add(formLayout2Col);
        formLayout2Col.add(buttonPrimary);
        formLayout2Col.add(buttonSecondary);
    }

    private void setGridSampleData(Grid grid) {
       // grid.setItems(query -> samplePersonService.list(VaadinSpringDataHelpers.toSpringPageRequest(query)).stream());
    }


}
