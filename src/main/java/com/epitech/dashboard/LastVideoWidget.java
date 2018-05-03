package com.epitech.dashboard;

import com.epitech.dashboard.youtube.LastVideoWidgetLayout;
import com.epitech.dashboard.youtube.VideoLayout;
import com.epitech.dashboard.youtube.YoutubeRequests;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItem;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.TextField;

import java.io.IOException;


public class LastVideoWidget extends AWidget {


    private LastVideoWidgetLayout widget = new VideoLayout();

    private YoutubeRequests requests = new YoutubeRequests();

    private TextField idField = new TextField();

    private Channel channel = null;

    public LastVideoWidget() {
        super("Last channel's video");
        formContent.addComponent(idField);
    }

    private LastVideoWidget(AWidget source) {
        super(source);
        if (source instanceof LastVideoWidget) {
            requests = ((LastVideoWidget) source).requests;
            idField = ((LastVideoWidget) source).idField;
            widget = new VideoLayout();
        } else
            throw new IllegalArgumentException(CLONE_ERR);
    }

    @Override
    public void refresh() {
        PlaylistItem video = requests.getChannelLastVideo(channel);
        try {
            ((VideoLayout)widget).getThumbnail().setSource(new ExternalResource(video.getSnippet().getThumbnails().getDefault().getUrl()));
            ((VideoLayout)widget).getTitle().setCaption(video.getSnippet().getTitle());
            ((VideoLayout)widget).getTitle().setResource(new ExternalResource(requests.buildVideoLink(video.getContentDetails().getVideoId())));
            ((VideoLayout)widget).getDate().setValue(video.getSnippet().getPublishedAt().toString());
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public AWidget clone() {
        return new LastVideoWidget(this);
    }

    @Override
    public void loadFromData(Widget source) {
        String m = (String) source.getInstance();
        JacksonFactory factory = new JacksonFactory();
        channel = new Channel();
        try {
            channel = factory.fromString(m, channel.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
        submitted();
    }

    @Override
    public Widget SaveWidget() {
        Widget save = new Widget();
        save.setType(this.getClass().getName());
        save.setInstance(channel.toString());
        return save;
    }

    /**
     * Sets the var channel based on the url
     * @param url Url of the channel
     */
    private void setChannel(String url)
    {
        ChannelListResponse response = requests.findChannel(url);
        if (response != null && !response.getItems().isEmpty())
            for (Channel item : response.getItems()) {
                channel = item;
            }
    }

    @Override
    public boolean submitted() {
        if (channel == null)
            setChannel(idField.getValue());
        refresh();
        mainDisplay = widget;
        return true;
    }
}
