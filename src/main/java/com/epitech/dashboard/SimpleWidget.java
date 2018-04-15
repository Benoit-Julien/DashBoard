package com.epitech.dashboard;

import com.vaadin.ui.*;

public class SimpleWidget extends AWidget {

    protected TextField nameField = new TextField();

    protected SimpleWidget(int uid, String name) {
        super(uid, "Youtube Subscriptors");
        formContent.addComponent(nameField);
    }

    public SimpleWidget(AWidget widget){
        super(widget);
        if (widget instanceof SimpleWidget){
            nameField = ((SimpleWidget) widget).nameField;
        }
        else
            throw new IllegalArgumentException("Cannot instantiate any widget to simple widget");
    }

    @Override
    public Window getFormWindow()
    {
        Window ret = super.getFormWindow();
        return ret;
    }

    @Override
    public void refresh() {
        //TODO: refresh the widget content
    }

    @Override
    public AWidget clone() {
        return new SimpleWidget(this);
    }

    @Override
    protected boolean submitted() {
        mainDisplay.addComponent(new Button(nameField.getValue()));
        return true;
    }
}
