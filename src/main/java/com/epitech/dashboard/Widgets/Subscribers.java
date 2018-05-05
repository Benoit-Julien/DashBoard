package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.Widget;
import com.epitech.dashboard.youtube.LastVideoWidgetLayout;
import com.epitech.dashboard.youtube.VideoLayout;
import com.epitech.dashboard.youtube.YoutubeRequests;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItem;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class Subscribers extends AWidget {
    private LastVideoWidgetLayout widget = new VideoLayout();
    private YoutubeRequests request = new YoutubeRequests();
    private TextField idField = new TextField();
    private String urlChannel = "";

    public Subscribers()
    {
        super("Number of Subscribers of a channel");
        formContent.addComponent(idField);
    }

    public Subscribers(AWidget source)
    {
        super(source);
        if (source instanceof Subscribers) {
            idField = ((Subscribers) source).idField;
            widget = new VideoLayout();
        }
        else
            throw new IllegalArgumentException(CLONE_ERR);
    }

    @Override
    public void refresh() {
        Channel channel = request.findChannel(urlChannel).getItems().get(0);
        try {
            ((VideoLayout)widget).getThumbnail().setSource(new ExternalResource(channel.getSnippet().getThumbnails().getMedium().getUrl()));
            ((VideoLayout)widget).getTitle().setCaption(channel.getSnippet().getTitle());
            ((VideoLayout)widget).getDate().setValue(channel.getStatistics().getSubscriberCount().toString());
        }catch (NullPointerException e)
        {
            e.printStackTrace();
        }

    }
    @Override
    public AWidget clone() {
        return new Subscribers(this);
    }

    @Override
    public void loadFromData(Widget source) {
        String url = (String) source.getInstance();
        urlChannel = url;
        submitted();
    }

    @Override
    public Widget SaveWidget() {
        Widget save = new Widget();
        save.setType(this.getClass().getName());
        save.setInstance(urlChannel);
        return save;
    }

    @Override
    public boolean submitted() {
        if (idField != null && !idField.getValue().isEmpty())
            urlChannel = idField.getValue();
        refresh();
        mainDisplay = widget;
        return true;
    }
}