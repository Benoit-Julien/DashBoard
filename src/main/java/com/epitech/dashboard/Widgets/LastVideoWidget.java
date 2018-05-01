package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.youtube.LastVideoWidgetLayout;
import com.epitech.dashboard.youtube.VideoLayout;
import com.epitech.dashboard.youtube.YoutubeRequests;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItem;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.TextField;



public class LastVideoWidget extends AWidget {


    private LastVideoWidgetLayout widget = new VideoLayout();

    private YoutubeRequests requests = new YoutubeRequests();

    private TextField idField = new TextField();

    private Channel channel = null;

    public LastVideoWidget(int uid) {
        super(uid, "Last channel's video");
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
        setChannel(idField.getValue());
        refresh();
        mainDisplay = widget;
        return true;
    }
}
