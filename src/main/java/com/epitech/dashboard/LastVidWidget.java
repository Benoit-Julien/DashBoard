package com.epitech.dashboard;

import com.epitech.dashboard.youtube.LastVideoWidget;
import com.epitech.dashboard.youtube.YoutubeRequests;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItem;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class LastVidWidget extends AWidget {

    class InnerWidget extends LastVideoWidget{
        public Image getThumbnail(){
            return thumbnail;
        }

        public Label getTitle(){
            return title;
        }

        public Label getDate(){
            return date;
        }
    }

    private LastVideoWidget widget = new InnerWidget();

    private YoutubeRequests requests = new YoutubeRequests();

    private TextField idField = new TextField();

    private Channel channel = null;

    public LastVidWidget(int uid) {
        super(uid, "Last channel's video");
        formContent.addComponent(idField);
    }

    private LastVidWidget(AWidget source) {
        super(source);
        if (source instanceof LastVidWidget) {
            requests = ((LastVidWidget) source).requests;
            idField = ((LastVidWidget) source).idField;
            widget = new InnerWidget();
        } else
            throw new IllegalArgumentException("Cannot instantiate any widget to last vid widget");
    }

    @Override
    public void refresh() {
        PlaylistItem video = requests.getChannelLastVideo(channel);
        try {
            ((InnerWidget)widget).getThumbnail().setSource(new ExternalResource(video.getSnippet().getThumbnails().getDefault().getUrl()));
            ((InnerWidget)widget).getTitle().setValue(video.getSnippet().getTitle());
            ((InnerWidget)widget).getDate().setValue(video.getSnippet().getPublishedAt().toString());
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public AWidget clone() {
        return new LastVidWidget(this);
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
