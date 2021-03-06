package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.Widget;
import com.epitech.dashboard.youtube.VideoLayout;
import com.epitech.dashboard.youtube.YoutubeRequests;
import com.google.api.services.youtube.model.Video;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.TextField;

public class VideoViews extends AWidget {

    VideoLayout layout = new VideoLayout();

    TextField urlForm = new TextField("Url de la video");

    boolean fromDb = false;

    YoutubeRequests requests = new YoutubeRequests();

    String url = "";

    public VideoViews() {
        formContent.addComponent(urlForm);
    }

    @Override
    public boolean refresh() {
        try {
            Video video = requests.VideoFromUrl(url);
            String title = video.getSnippet().getTitle();
            if (title.length() > 20)
                title = title.substring(0, 19).concat("...");
            layout.getThumbnail().setSource(new ExternalResource(video.getSnippet().getThumbnails().getDefault().getUrl()));
            layout.getTitle().setCaption(title);
            layout.getTitle().setResource(new ExternalResource(requests.buildVideoLink(video.getId())));
            layout.getDate().setValue("Nombre de vues: ".concat(video.getStatistics().getViewCount().toString()));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void loadFromData(Widget source) {
        url = (String) source.getInstance();
        fromDb = true;
        submitted();
    }

    @Override
    public Widget SaveWidget() {
        Widget save = new Widget();
        save.setInstance(url);
        save.setType(this.getClass().getName());
        return save;
    }

    @Override
    public boolean submitted() {
        mainDisplay = layout;
        if (!fromDb)
            url = urlForm.getValue();
        return refresh();
    }
}
