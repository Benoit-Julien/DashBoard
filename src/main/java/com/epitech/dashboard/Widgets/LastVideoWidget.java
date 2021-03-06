package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.Widget;
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


    private VideoLayout widget = new VideoLayout();

    private YoutubeRequests requests = new YoutubeRequests();

    private TextField idField = new TextField("Url de la chaine : ");

    private Channel channel = null;

    public LastVideoWidget() {
        formContent.addComponent(idField);
    }

    @Override
    public boolean refresh() {
        PlaylistItem video = requests.getChannelLastVideo(channel);
        try {
            String title = video.getSnippet().getTitle();
            if (title.length() > 20)
                title = title.substring(0, 19).concat("...");
            widget.getThumbnail().setSource(new ExternalResource(video.getSnippet().getThumbnails().getDefault().getUrl()));
            widget.getTitle().setCaption(title);
            widget.getTitle().setResource(new ExternalResource(requests.buildVideoLink(video.getContentDetails().getVideoId())));
            widget.getDate().setValue("Dernière video de la chaîne: ".concat(channel.getSnippet().getTitle()));
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
     *
     * @param url Url of the channel
     */
    private void setChannel(String url) {
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
        mainDisplay = widget;
        return refresh();
    }
}
