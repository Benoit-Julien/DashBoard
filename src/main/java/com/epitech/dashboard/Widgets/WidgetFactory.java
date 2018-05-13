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
            widgets.put("Derniere video (YT)", Class.forName("com.epitech.dashboard.Widgets.LastVideoWidget"));
            widgets.put("Flux RSS", Class.forName("com.epitech.dashboard.Widgets.RSSFeedWidget"));
            widgets.put("Nombre de fans sur Deezer", Class.forName("com.epitech.dashboard.Widgets.Deezer"));
            widgets.put("Nombre d'abonnes (YT)", Class.forName("com.epitech.dashboard.Widgets.Subscribers"));
            widgets.put("Nombre de vues dans une chaîne (YT)", Class.forName("com.epitech.dashboard.Widgets.ViewsNumber"));
            widgets.put("Top tendances (YT)", Class.forName("com.epitech.dashboard.Widgets.TopTrendingWidget"));
            widgets.put("Nombre de vues dans une video", Class.forName("com.epitech.dashboard.Widgets.VideoViews"));
            widgets.put("Météo et horloge", Class.forName("com.epitech.dashboard.Widgets.WeatherAndTimeWidget"));
            widgets.put("Bourse", Class.forName("com.epitech.dashboard.Widgets.StockQuoteWidget"));
            widgets.put("Nombre d'albums d'un artiste", Class.forName("com.epitech.dashboard.Widgets.NumberAlbums"));
            widgets.put("Nombre de commentaires (YT)", Class.forName("com.epitech.dashboard.Widgets.NbrCommentYT"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        models = new ListDataProvider<>(widgets.keySet());
    }
}
