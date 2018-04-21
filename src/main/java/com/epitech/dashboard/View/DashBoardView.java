package com.epitech.dashboard.View;

import com.epitech.dashboard.AWidget;
import com.epitech.dashboard.LastVideoWidget;
import com.epitech.dashboard.TopTrendingWidget;
import com.epitech.dashboard.User;
import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.vaadin.annotations.Theme;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringView(name = DashBoardView.VIEW_NAME)
public class DashBoardView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "dashboard";
    public static User currentUser;

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
    private ResponsiveLayout widgetsGrid = new ResponsiveLayout();

    /**
     * Window containing the form for a widget (null or last widget instantiated form)
     */
    private PopupView formWindow = null;

    /**
     * ComoBox Displaying the available
     */
    private ComboBox<AWidget> select = new ComboBox<>("Select a widget");


    @PostConstruct
    private void init() {
        Button button = new Button("Add a widget", VaadinIcons.PLUS);
        VerticalLayout popupContent = new VerticalLayout();
        PopupView selectWidgets = new PopupView(null, popupContent);

        select.setWidth("100%");
        select.setDataProvider(models);
        select.setTextInputAllowed(false);
        select.setItemCaptionGenerator(AWidget::getName);
        selectWidgets.setWidth("500px");
        popupContent.addComponent(select);
        addComponent(selectWidgets);
        setMargin(true);
        setSpacing(true);
        addComponent(button);
        setComponentAlignment(selectWidgets, Alignment.TOP_CENTER);
        addComponent(widgetsGrid);
        select.addSelectionListener(this::selectionListener);
        button.addClickListener(e -> selectWidgets.setPopupVisible(true));
        LastVideoWidget simple = new LastVideoWidget(0);
        TopTrendingWidget two = new TopTrendingWidget(1);
        models.getItems().add(simple);
        models.getItems().add(two);
        two.addSubmitListener(e -> submitListener(e, two.clone()));
        simple.addSubmitListener(e -> submitListener(e, simple.clone()));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }


    /**
     * Listener to be attached to every widget model
     * @param event Event to be handled
     * @param widget Widget instantiated
     */
    private void submitListener(Button.ClickEvent event, AWidget widget)
    {
        formWindow.setPopupVisible(false);
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
        removeComponent(formWindow);
        addComponent(formWindow);
        formWindow.setSizeFull();
        formWindow.setPopupVisible(true);
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
