package com.epitech.dashboard.RSSFeed;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.annotations.DesignRoot;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.declarative.Design;
import com.vaadin.ui.Link;
import com.vaadin.ui.Label;
import com.vaadin.ui.Image;
import com.vaadin.ui.VerticalLayout;

/**
 * !! DO NOT EDIT THIS FILE !!
 * <p>
 * This class is generated by Vaadin Designer and will be overwritten.
 * <p>
 * Please make a subclass with logic and additional interfaces as needed,
 * e.g class LoginView extends LoginDesign implements View { }
 */
@DesignRoot
@AutoGenerated
@SuppressWarnings("serial")
public class RSSLayout extends AbsoluteLayout {
    protected VerticalLayout verticalLayout;
    protected Link title;
    protected Image image;
    protected Label desc;
    protected Label date;

    public RSSLayout() {
        Design.read(this);
    }
}