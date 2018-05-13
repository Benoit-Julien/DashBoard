package com.epitech.dashboard.WeatherTime;

import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class WeatherTimeWidgetLayout extends WeatherTimeLayout {
    public VerticalLayout getVerticalLayout() {
        return verticalLayout;
    }

    public Label getClock() {
        return clock;
    }

    public Label getCity() {
        return city;
    }

    public Image getImage() {
        return image;
    }

    public Label getTemperature() {
        return temperature;
    }

    public Label getWind() {
        return wind;
    }
}
