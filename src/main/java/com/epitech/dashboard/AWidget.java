package com.epitech.dashboard;

import com.vaadin.ui.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

abstract public class AWidget {

    /**
     * Name to be displayed in the combo box
     */
    private String name;

    /**
     * Popup view to contain the form which will instantiate a widget
     */
    protected PopupView formWindow = null;

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
    protected FormLayout formContent = new FormLayout();

    /**
     * Submit button
     */
    private Button submitButton = new Button("Submit");

    /**
     * Constructor mainly to use in case of instantiation of a model
     * @param name Unique name of the model
     */
    protected AWidget(String name){
        this.name = name;
        submitButton.addClickListener(e -> this.submitted());
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(formContent);
        formWindow = new PopupView(null, layout);
        layout.setComponentAlignment(formContent, Alignment.MIDDLE_CENTER);
    }

    /**
     * Adds a listener to the submit button
     * @param listener Listener to be attached
     */
    public void addSubmitListener(Button.ClickListener listener){
        submitButton.addClickListener(listener);
    }

    /**
     * Copy constructor, you have to override this & call him
     * do not forget to copy your form fields if you want to access them
     * more easy
     * @param widget Widget to copy
     */
    public AWidget(AWidget widget){
        name = widget.name;
        mainDisplay = new AbsoluteLayout();
        formContent = widget.formContent;
    }

    /**
     * Getter for the form window
     * @return Window to be displayed
     */
    public PopupView getFormWindow(){
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
     * Clone the model into a viable widget
     * @return A cloned widget
     */
    public abstract AWidget clone();

    /**
     * Initializes the widget with all the necessary data
     * @param source Data to instantiate from
     */
    public abstract void loadFromData(Widget source);

    /**
     * Instantiate a widget object with the necessary
     * data to re-instantiate it later
     * @return Widget to be stored
     */
    public abstract Widget SaveWidget();

    /**
     * Method will be called when the form is submitted
     */
    public abstract boolean submitted();

    public Component getComponent(){
        return mainDisplay;
    }

    /**
     * @return Name of the widget
     */
    public String getName() {
        return name;
    }
}
