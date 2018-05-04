package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.Widget;

public class WeatherAndTimeWidget extends AWidget {

    public WeatherAndTimeWidget(int uid)
    {
        super("Weather and Time");
    }

    @Override
    public void refresh() {

    }

    @Override
    public AWidget clone() {
        return null;
    }

    @Override
    public void loadFromData(Widget source) {

    }

    @Override
    public Widget SaveWidget() {
        return null;
    }

    @Override
    public boolean submitted() {
        return false;
    }
}
