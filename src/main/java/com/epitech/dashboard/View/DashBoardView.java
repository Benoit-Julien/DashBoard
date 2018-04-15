package com.epitech.dashboard.View;

import com.epitech.dashboard.User;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.*;

import javax.annotation.PostConstruct;

@SpringView(name = DashBoardView.VIEW_NAME)
public class DashBoardView extends VerticalLayout implements View {
    public static final String VIEW_NAME = "dashboard";
    public static User currentUser;

    @PostConstruct
    private void init() {
        addComponent(new Label("Dashboard " + currentUser.getFirstName()));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {
        // This view is constructed in the init() method()
    }
}
