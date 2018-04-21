package com.epitech.dashboard;

import com.epitech.dashboard.youtube.LastVideoWidgetLayout;
import com.epitech.dashboard.youtube.VideoLayout;
import com.epitech.dashboard.youtube.YoutubeRequests;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.Video;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.ComboBox;

public class TopTrendingWidget extends AWidget {

    private ListDataProvider<YoutubeRequests.Region> countries;

    private ComboBox<YoutubeRequests.Region> countrySelect = new ComboBox<>("Select a country");

    private YoutubeRequests requests = new YoutubeRequests();

    private YoutubeRequests.Region country = null;

    private LastVideoWidgetLayout widget = new VideoLayout();

    public TopTrendingWidget(int uid) {
        super(uid, "Top trending video youtube");
        countries = new ListDataProvider<>(requests.getCountriesList());
        countrySelect.setDataProvider(countries);
        formContent.addComponent(countrySelect);
        formContent.setHeight("500px");
    }

    private TopTrendingWidget(AWidget source){
        super(source);
        if (source instanceof TopTrendingWidget) {
            countrySelect = ((TopTrendingWidget) source).countrySelect;
            countries = ((TopTrendingWidget) source).countries;
            widget = new VideoLayout();
        } else
            throw new IllegalArgumentException(CLONE_ERR);
    }

    @Override
    public void refresh() {
        Video video = requests.getTopTrendingVideo(country);
        try {
            ((VideoLayout)widget).getThumbnail().setSource(new ExternalResource(video.getSnippet().getThumbnails().getDefault().getUrl()));
            ((VideoLayout)widget).getTitle().setCaption(video.getSnippet().getTitle());
            ((VideoLayout)widget).getTitle().setResource(new ExternalResource(requests.buildVideoLink(video.getId())));
            ((VideoLayout)widget).getDate().setValue(video.getSnippet().getDescription());
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public AWidget clone() {
        return new TopTrendingWidget(this);
    }

    @Override
    public boolean submitted() {
        country = countrySelect.getValue();
        mainDisplay = widget;
        refresh();
        return true;
    }
}
