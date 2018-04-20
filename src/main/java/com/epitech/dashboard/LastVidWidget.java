package com.epitech.dashboard;

import com.epitech.dashboard.youtube.YoutubeRequests;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.FileResource;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

import java.io.File;

public class LastVidWidget extends AWidget {

    YoutubeRequests requests = new YoutubeRequests();

    protected TextField idField = new TextField();

    private Channel channel = null;

    Label label = new Label("This worked a little");

    public LastVidWidget(int uid) {
        super(uid, "Last channel's video");
        mainDisplay.addComponent(label);
        formContent.addComponent(idField);
    }

    private LastVidWidget(AWidget widget) {
        super(widget);
        if (widget instanceof LastVidWidget) {
            requests = ((LastVidWidget) widget).requests;
            idField = ((LastVidWidget) widget).idField;
        } else
            throw new IllegalArgumentException("Cannot instantiate any widget to last vid widget");
    }

    @Override
    public void refresh() {
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
        Image img = new Image(channel.getSnippet().getTitle(), new ExternalResource(channel.getSnippet().getThumbnails().getDefault().getUrl()));
        label.setValue(channel.getSnippet().getTitle());
        mainDisplay.addComponent(img);
        return true;
    }
}
