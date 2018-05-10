package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.Widget;
import com.epitech.dashboard.youtube.LastVideoWidgetLayout;
import com.epitech.dashboard.youtube.Region;
import com.epitech.dashboard.youtube.VideoLayout;
import com.epitech.dashboard.youtube.YoutubeRequests;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.services.youtube.model.Video;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.ComboBox;

public class TopTrendingWidget extends AWidget {

    private ListDataProvider<Region> countries;

    private ComboBox<Region> countrySelect = new ComboBox<>("Select a country");

    private YoutubeRequests requests = new YoutubeRequests();

    private Region country = null;

    private LastVideoWidgetLayout widget = new VideoLayout();

    public TopTrendingWidget() {
        countries = new ListDataProvider<>(requests.getCountriesList());
        countrySelect.setDataProvider(countries);
        formContent.addComponent(countrySelect);
    }

    @Override
    public boolean refresh() {
        Video video = requests.getTopTrendingVideo(country);
        try {
            String title = video.getSnippet().getTitle();
            if (title.length() > 20)
                title = title.substring(0, 19).concat("...");
            ((VideoLayout) widget).getThumbnail().setSource(new ExternalResource(video.getSnippet().getThumbnails().getDefault().getUrl()));
            ((VideoLayout) widget).getTitle().setCaption(title);
            ((VideoLayout) widget).getTitle().setResource(new ExternalResource(requests.buildVideoLink(video.getId())));
            ((VideoLayout) widget).getDate().setValue("Top trending video in: ".concat(country.getName()));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void loadFromData(Widget source) {
        ObjectMapper mapper = new ObjectMapper();
        country = mapper.convertValue(source.getInstance(), Region.class);
        submitted();
    }

    @Override
    public Widget SaveWidget() {
        Widget save = new Widget();
        save.setInstance(country);
        save.setType(this.getClass().getName());
        return save;
    }

    @Override
    public boolean submitted() {
        if (country == null)
            country = countrySelect.getValue();
        mainDisplay = widget;
        return refresh();
    }
}
