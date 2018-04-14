package com.epitech.dashboard;

import com.vaadin.ui.*;

abstract public class AWidget {

    protected class MainLayout extends VerticalLayout{
        public static final String CLASSNAME = "MainLayout";

        public MainLayout(){
            setStyleName(CLASSNAME);
            setWidth("200px");
            setHeight("200px");
        }
    }

    /**
     * Name to be displayed in the combo box
     */
    private String name;

    /**
     * Integer to give an unique id to all children classes
     */
    private int uid;

    /**
     * Popup view to contain the form which will instantiate a widget
     */
    protected Window formWindow = new Window();

    /**
     * Main component, this is the component who must be displayed
     */
    protected MainLayout mainDisplay = new MainLayout();

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
     * @param uid Unique Id of the model
     * @param name Unique name of the model
     */
    protected AWidget(int uid, String name){
        this.uid = uid;
        this.name = name;
        submitButton.addClickListener(e -> this.submitted());
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(formContent);
        formWindow.setContent(layout);
        layout.setComponentAlignment(formContent, Alignment.MIDDLE_CENTER);
        formWindow.center();
    }

    /**
     * Adds a listener to the submit button
     * @param listener Listener to be attached
     */
    public void addSubmitListener(Button.ClickListener listener){
        submitButton.addClickListener(listener);
    }

    /**
     * Copy constructor
     * @param widget Widget to copy
     */
    public AWidget(AWidget widget){
        uid = widget.uid;
        name = widget.name;
        mainDisplay = new MainLayout();
        formContent = widget.formContent;
    }

    /**
     * Unique id getter
     * @return Unique id of the model
     */
    public int getUid() {
        return uid;
    }

    /**
     * Getter for the form window
     * @return Window to be displayed
     */
    public Window getFormWindow(){
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
     * Method will be called when the form is submitted
     */
    protected abstract boolean submitted();

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
