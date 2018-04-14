package com.epitech.dashboard;

import com.vaadin.annotations.Theme;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.selection.SelectionEvent;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@SpringUI
@Theme("valo")
public class VaadinUI extends UI {

    /**
     * TODO: Implement doc about this #Julien
     */
    private Service service;

    /**
     * Main layout of the page
     */
    private VerticalLayout layout = new VerticalLayout();

    /**
     * List of widgets linked to the widgets grid
     */
    private List<AWidget> widgets = new ArrayList<>();

    /**
     * List of widgets models
     */
    private ListDataProvider<AWidget> models = new ListDataProvider<>(new ArrayList<>());

    /**
     * Grid containing the instantiated widgets
     */
    private GridLayout widgetsGrid = new GridLayout();

    /**
     * Window containing the form for a widget (null or last widget instantiated form)
     */
    private Window formWindow = new Window();

    /**
     * ComoBox Displaying the available
     */
    private ComboBox<AWidget> select = new ComboBox<>("Select a widget");

    /**
     * In this beta the models have to be instantiated here
     * do not forget to add it to the models and instantiate the submit listener as in the example
     */
    private void initModels() {
        //SimpleWidget widget = new SimpleWidget(0, String.valueOf(Math.random() * (30000 - 0)));

        //models.getItems().add(widget);
        //widget.addSubmitListener(e -> submitListener(e, widget.clone()));
    }

    /**
     * Initializes the layouts of the page
     */
    private void initLayouts(){
        Button button = new Button("Add a widget", VaadinIcons.PLUS);
        VerticalLayout popupContent = new VerticalLayout();
        PopupView selectWidgets = new PopupView(null, popupContent);

        select.setWidth("100%");
        select.setDataProvider(models);
        select.setTextInputAllowed(false);
        select.setItemCaptionGenerator(AWidget::getName);
        selectWidgets.setWidth("500px");
        popupContent.addComponent(select);
        layout.addComponent(selectWidgets);
        layout.setMargin(true);
        layout.setSpacing(true);
        layout.addComponent(button);
        layout.setComponentAlignment(selectWidgets, Alignment.TOP_CENTER);
        layout.addComponent(widgetsGrid);
        setContent(layout);
        select.addSelectionListener(this::selectionListener);
        button.addClickListener(e -> selectWidgets.setPopupVisible(true));
    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        initLayouts();
        initModels();
    }

    /**
     * Listener to be attached to every widget model
     * @param event Event to be handled
     * @param widget Widget instantiated
     */
    private void submitListener(Button.ClickEvent event, AWidget widget)
    {
        formWindow.close();
        widget.submitted();
        addWidget(widget);
        select.setValue(null);
    }

    /**
     * Triggers when an item from the combo box list is selected
     * @param event Event to be handled
     */
    private void selectionListener(SingleSelectionEvent<AWidget> event) {
        AWidget widget = event.getSelectedItem().orElse(null);
        if (widget == null)
            return;
        formWindow = widget.getFormWindow();
        formWindow.center();
        removeWindow(formWindow);
        addWindow(formWindow);
    }

    /**
     * Adds a widget to the list
     * @param widget Initialized widget
     */
    private void addWidget(AWidget widget) {
        if (widget == null)
            return;
        widgets.add(widget);
        updateGrid();
    }

    /**
     * Updates the widgets grid
     */
    private void updateGrid() {
        widgetsGrid.removeAllComponents();
        int n = 0;
        for (AWidget widget : widgets) {
            Component comp = widget.getComponent();
            comp.setId(String.valueOf(n));
            widgetsGrid.addComponent(comp);
            n++;
        }
    }
}