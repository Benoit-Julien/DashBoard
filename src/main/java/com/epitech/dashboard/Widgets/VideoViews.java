package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.Widget;
import com.epitech.dashboard.youtube.VideoLayout;
import com.epitech.dashboard.youtube.YoutubeRequests;
import com.google.api.services.youtube.model.Video;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.TextField;

public class VideoViews extends AWidget {

    VideoLayout layout = new VideoLayout();

    TextField  urlForm = new TextField();

    boolean fromDb = false;

    YoutubeRequests requests = new YoutubeRequests();

    String url = "";

    public VideoViews(){
        super("Yt views counter");
        formContent.addComponent(urlForm);
    }

    private VideoViews(AWidget source) {
        super(source);
        if (source instanceof VideoViews) {
            urlForm = ((VideoViews) source).urlForm;
        } else
            throw new IllegalArgumentException(CLONE_ERR);
    }

    @Override
    public void refresh() {
        Video  video = requests.VideoFromUrl(url);
        String title = video.getSnippet().getTitle();
        if (title.length() > 20)
            title = title.substring(0, 19).concat("...");
        layout.getThumbnail().setSource(new ExternalResource(video.getSnippet().getThumbnails().getDefault().getUrl()));
        layout.getTitle().setCaption(title);
        layout.getTitle().setResource(new ExternalResource(requests.buildVideoLink(video.getId())));
        layout.getDate().setValue("Nb of views: ".concat(video.getStatistics().getViewCount().toString()));
    }

    @Override
    public AWidget clone() {
        return new VideoViews(this);
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
        refresh();
        return true;
    }
}
