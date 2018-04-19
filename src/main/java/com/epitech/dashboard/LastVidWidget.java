package com.epitech.dashboard;

public class LastVidWidget extends AWidget {

    protected LastVidWidget(int uid, String name) {
        super(uid, name);
    }

    public LastVidWidget(AWidget widget){
        super(widget);
        if (widget instanceof LastVidWidget){
        }
        else
            throw new IllegalArgumentException("Cannot instantiate any widget to last vid widget");
    }

    @Override
    public void refresh() {

    }

    @Override
    public AWidget clone() {
        return null;
    }

    @Override
    public boolean submitted() {
        return false;
    }
}
