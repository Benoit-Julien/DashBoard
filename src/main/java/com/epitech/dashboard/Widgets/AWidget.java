package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.Widget;
import com.vaadin.ui.*;

abstract public class AWidget {

    /**
     * Popup view to contain the form which will instantiate a widget
     */
    private PopupView formWindow;

    /**
     * Error to be thrown when cloning unmatched widgets
     */
    protected static final String CLONE_ERR = "Source widget and destination widget do not match, impossible to clone!";

    /**
     * Main component, this is the component who must be displayed
     */
    protected AbsoluteLayout mainDisplay = new AbsoluteLayout();

    /**
     * Layout to contain the form to instantiate a widget
     */
    protected FormLayout formContent;

    /**
     * Submit button
     */
    private Button submitButton;

    /**
     * Constructor mainly to use in case of instantiation of a model
     */
    protected AWidget() {
        formContent = new FormLayout();

        submitButton = new Button("Submit");
        submitButton.addClickListener(e -> this.submitted());

        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(formContent);
        layout.setComponentAlignment(formContent, Alignment.MIDDLE_CENTER);

        formWindow = new PopupView(null, layout);
    }

    /**
     * Adds a listener to the submit button
     *
     * @param listener Listener to be attached
     */
    public void addSubmitListener(Button.ClickListener listener) {
        submitButton.addClickListener(listener);
    }

    /**
     * Getter for the form window
     *
     * @return Window to be displayed
     */
    public PopupView getFormWindow() {
        formContent.addComponent(submitButton);
        formContent.setComponentAlignment(submitButton, Alignment.BOTTOM_LEFT);
        return formWindow;
    }

    /**
     * This method must be able to refresh the content of the widget
     * it will be called each minute
     */
    public abstract void refresh();

    /**
     * Initializes the widget with all the necessary data
     *
     * @param source Data to instantiate from
     */
    public abstract void loadFromData(Widget source);

    /**
     * Instantiate a widget object with the necessary
     * data to re-instantiate it later
     *
     * @return Widget to be stored
     */
    public abstract Widget SaveWidget();

    /**
     * Method will be called when the form is submitted
     */
    public abstract boolean submitted();

    public Component getComponent() {
        return mainDisplay;
    }
}
