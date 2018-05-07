package com.epitech.dashboard.RSSFeed;

import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;

public class RSSWidgetLayout extends RSSLayout {
    public Link getTitle() {
        return title;
    }

    public Image getImage() {
        return image;
    }

    public Label getDesc() {
        return desc;
    }

    public Label getDate() {
        return date;
    }

    public RSSWidgetLayout() {
        super();
        title.addStyleName("cut_text");
        desc.addStyleName("cut_text");
        verticalLayout.setMargin(false);
    }
}
