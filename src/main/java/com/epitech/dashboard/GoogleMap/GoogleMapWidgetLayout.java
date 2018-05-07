package com.epitech.dashboard.GoogleMap;

import com.vaadin.ui.Image;
import com.vaadin.ui.Label;

public class GoogleMapWidgetLayout extends GoogleMapLayout {
    public Image getImage() {
        return image;
    }

    public Label getDistance() {
        return distance;
    }

    public Label getDuration() {
        return duration;
    }
}
