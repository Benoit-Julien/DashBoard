package com.epitech.dashboard;

import com.vaadin.ui.*;

abstract public class AWidget {

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
    protected Component mainDisplay = null;

    /**
     * Layout to contain the form to instantiate a widget
     */
    protected FormLayout formContent = new FormLayout();

    /**
     * Submit button
     */
    private Button submitButton = new Button("Submit");

    protected AWidget(int uid, String name){
        this.uid = uid;
        this.name = name;
        submitButton.addClickListener(e -> this.submitted());
        formWindow.setContent(formContent);
        formWindow.center();
        formWindow.setSizeFull();
    }

    public int getUid() {
        return uid;
    }

    /**
     *
     * @return Window to be displayed
     */
    public Window getFormWindow(){
        formContent.addComponent(submitButton);
        return formWindow;
    }

    /**
     * This method must be able to refresh the content of the widget
     * it will be called each minute
     */
    public abstract void refresh();

    /**
     * Method will be called when the form is ok
     */
    protected abstract void submitted();

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
