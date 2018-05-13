package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.Movies.MoviesWidgetLayout;
import com.epitech.dashboard.MoviesDB.Genre;
import com.epitech.dashboard.Widget;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.ComboBox;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MoviesWidget extends AWidget {

    static final String API_KEY = "0ee760057b72c9726189de2a809f4a94";
    static final String MOVIE_URL_FIRST = "https://api.themoviedb.org/3/genre/";
    static final String MOVIE_URL_SECOND = "/movies?api_key=0ee760057b72c9726189de2a809f4a94&language=fr-FR&include_adult=false&sort_by=created_at.asc";
    static final String GENRE_URL = "https://api.themoviedb.org/3/genre/movie/list?api_key=0ee760057b72c9726189de2a809f4a94&language=fr-FR";
    private MoviesWidgetLayout layout = new MoviesWidgetLayout();
    Genre genre = null;

    ComboBox<Genre> genresForm = new ComboBox<Genre>("Selectionnez le genre a visualiser : ");
    ListDataProvider<Genre> data = null;

    public MoviesWidget() {
        try {
            RestTemplate template = new RestTemplate();
            LinkedHashMap res = template.getForObject(GENRE_URL, LinkedHashMap.class);
            data = new ListDataProvider<>(Genre.GetGenres((List) res.get("genres")));
            genresForm.setDataProvider(data);
            formContent.addComponent(genresForm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean refresh() {
        try {
            RestTemplate template = new RestTemplate();
            LinkedHashMap res = template.getForObject((MOVIE_URL_FIRST + genre.getId() + MOVIE_URL_SECOND), LinkedHashMap.class);
            ArrayList list = (ArrayList) res.get("results");
            ObjectMapper mapper = new ObjectMapper();
            for (Object map : list) {
                if (map instanceof LinkedHashMap) {
                    String header = "Genre: ".concat(genre.getName());
                    layout.getHead().setValue(header);
                    layout.getTitle().setValue((String) ((LinkedHashMap) map).get("title"));
                    layout.getDate().setValue((String) ((LinkedHashMap) map).get("release_date"));
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void loadFromData(Widget source) {
        ObjectMapper mapper = new ObjectMapper();
        genre = mapper.convertValue(source.getInstance(), Genre.class);
        submitted();
    }

    @Override
    public Widget SaveWidget() {
        Widget save = new Widget();
        save.setInstance(genre);
        save.setType(this.getClass().getName());
        return save;
    }

    @Override
    public boolean submitted() {
        if (genre == null)
            genre = genresForm.getValue();
        mainDisplay = layout;
        refresh();
        return true;
    }
}
