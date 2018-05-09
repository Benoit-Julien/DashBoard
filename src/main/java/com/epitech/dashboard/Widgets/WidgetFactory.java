package com.epitech.dashboard.Widgets;

import com.vaadin.data.provider.ListDataProvider;

import java.util.HashMap;

public class WidgetFactory {
    private static WidgetFactory ourInstance = new WidgetFactory();

    public static WidgetFactory getInstance() {
        return ourInstance;
    }

    public HashMap<String, Class> widgets;

    /**
     * List of widgets models
     */
    public ListDataProvider<String> models;

    private WidgetFactory() {
        widgets = new HashMap<>();
        try {
            widgets.put("Google Map", Class.forName("com.epitech.dashboard.Widgets.GoogleMapWidget"));
            widgets.put("Last channel's video", Class.forName("com.epitech.dashboard.Widgets.LastVideoWidget"));
            widgets.put("Flux RSS", Class.forName("com.epitech.dashboard.Widgets.RSSFeedWidget"));
            widgets.put("Number of Subscribers of a channel", Class.forName("com.epitech.dashboard.Widgets.Subscribers"));
            widgets.put("Top trending video youtube", Class.forName("com.epitech.dashboard.Widgets.TopTrendingWidget"));
            widgets.put("Yt views counter", Class.forName("com.epitech.dashboard.Widgets.VideoViews"));
            widgets.put("Météo et horloge", Class.forName("com.epitech.dashboard.Widgets.WeatherAndTimeWidget"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        models = new ListDataProvider<>(widgets.keySet());
    }
}
