package com.epitech.dashboard.View;

import com.epitech.dashboard.User;
import com.epitech.dashboard.Widget;
import com.epitech.dashboard.WidgetRepository;
import com.epitech.dashboard.Widgets.AWidget;
import com.epitech.dashboard.Widgets.WidgetFactory;
import com.jarektoro.responsivelayout.ResponsiveLayout;
import com.vaadin.event.selection.SingleSelectionEvent;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

@SpringView(name = DashBoardView.VIEW_NAME)
public class DashBoardView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "dashboard";
    public static User currentUser;
    private boolean stop = false;

    @Autowired
    private WidgetRepository widgetRepository;

    public class RefreshThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    stop = sleepToUpdate();
                    Runnable uiRunnable = () -> {
                        for (AWidget widget : widgets) {
                            widget.refresh();
                        }
                    };
                    getUI().access(uiRunnable);
                    getUI().push();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private boolean sleepToUpdate() {
            try {
                Thread.sleep(30000);
            } catch (final InterruptedException ignore) {
                ignore.printStackTrace();
            }
            return true;
        }
    }

    /**
     * List of widgets linked to the widgets grid
     */
    private List<AWidget> widgets = new ArrayList<>();

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
    private ComboBox<String> select = new ComboBox<>("Sélectionner un widget");

    private PopupView selectWidgetPopup;

    @PostConstruct
    private void init() {
        Button add = new Button("Ajouter un widget", VaadinIcons.PLUS);
        Button refresh = new Button("Rafraichir");
        HorizontalLayout buttonLayout = new HorizontalLayout(add, refresh);

        VerticalLayout popupContent = new VerticalLayout();
        selectWidgetPopup = new PopupView(null, popupContent);

        //region Init static content
        select.setWidth("100%");
        select.setDataProvider(WidgetFactory.getInstance().models);
        select.setTextInputAllowed(false);
        select.setEmptySelectionAllowed(false);
        select.addSelectionListener(this::selectionListener);

        popupContent.addComponent(select);
        selectWidgetPopup.setHideOnMouseOut(false);
        addComponent(selectWidgetPopup);

        setMargin(true);
        setSpacing(true);
        addComponent(buttonLayout);
        setComponentAlignment(selectWidgetPopup, Alignment.TOP_CENTER);
        addComponent(widgetsGrid);
        add.addClickListener(e -> selectWidgetPopup.setPopupVisible(true));
        refresh.addClickListener(e -> {
            for (AWidget widget : widgets) {
                widget.refresh();
            }
        });
        //endregion

        //region Init dynamic widgets
        List<Widget> widgetList = null;
        try {
            widgetList = widgetRepository.findWidgetsByOwner(currentUser);
            for (Widget data : widgetList) {
                try {
                    Class<?> clazz = Class.forName(data.getType());
                    Constructor<?> constructor = clazz.getConstructor();
                    AWidget instance = (AWidget) constructor.newInstance();
                    instance.loadFromData(data);
                    instance.setId(data.getId());
                    addWidget(instance);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateGrid();
        //endregion

        //region Update thread
        new RefreshThread().start();
        //endregion

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }


    /**
     * Listener to be attached to every widget model
     *
     * @param event  Event to be handled
     * @param widget Widget instantiated
     */
    private void submitListener(Button.ClickEvent event, AWidget widget) {
        try {
            formWindow.setPopupVisible(false);
            if (widget.submitted()) {
                Widget save = widget.SaveWidget();
                save.setOwner(currentUser);
                widgetRepository.save(save);
                widget.setId(save.getId());
                addWidget(widget);
                select.setValue(null);
            } else
                Notification.show("Erreur lors de la création du Widget");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Triggers when an item from the combo box list is selected
     *
     * @param event Event to be handled
     */
    private void selectionListener(SingleSelectionEvent<String> event) {
        String widgetkey = event.getSelectedItem().orElse(null);
        if (widgetkey == null)
            return;
        try {
            Class<?> clazz = WidgetFactory.getInstance().widgets.get(widgetkey);
            Constructor<?> constructor = clazz.getConstructor();

            final AWidget widget = (AWidget) constructor.newInstance();

            widget.addSubmitListener(e -> submitListener(e, widget));
            formWindow = widget.getFormWindow();
            formWindow.setHideOnMouseOut(false);
            formWindow.addPopupVisibilityListener(popupVisibilityEvent -> {
                if (!popupVisibilityEvent.isPopupVisible())
                    removeComponent(formWindow);
            });
            addComponent(formWindow, 0);
            formWindow.setSizeFull();
            formWindow.setPopupVisible(true);
            select.setValue(null);
            selectWidgetPopup.setPopupVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a widget to the list
     *
     * @param widget Initialized widget
     */
    private void addWidget(AWidget widget) {
        if (widget == null)
            return;
        widget.addDeleteButtonListener(clickEvent -> {
            widgetRepository.deleteById(widget.getId());
            widgets.remove(widget);
            widgetsGrid.removeComponent(widget.getComponent());
        });
        widgets.add(widget);
        updateGrid();
    }

    /**
     * Updates the widgets grid
     */
    private void updateGrid() {
        for (AWidget widget : widgets) {
            if (widgetsGrid.getComponentIndex(widget.getComponent()) == -1)
                widgetsGrid.addComponent(widget.getComponent());
            else
                widgetsGrid.replaceComponent(widget.getComponent(), widget.getComponent());
        }
    }
}
