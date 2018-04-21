package com.epitech.dashboard.youtube;

import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;

public class VideoLayout extends LastVideoWidgetLayout {
    public Image getThumbnail(){
        return thumbnail;
    }

    public Link getTitle(){
        return title;
    }

    public Label getDate(){
        return date;
    }
}