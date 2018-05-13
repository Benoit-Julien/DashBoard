package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.Widget;
import com.epitech.dashboard.youtube.VideoLayout;
import com.epitech.dashboard.youtube.YoutubeRequests;
import com.google.api.services.youtube.model.Channel;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.TextField;

public class SubscribersWidget extends AWidget {
    private VideoLayout widget = new VideoLayout();
    private YoutubeRequests request = new YoutubeRequests();
    private TextField idField = new TextField("Url de la chaine : ");
    private String urlChannel = "";

    public SubscribersWidget() {
        formContent.addComponent(idField);
    }

    @Override
    public boolean refresh() {
        Channel channel = request.findChannel(urlChannel).getItems().get(0);
        try {
            widget.getThumbnail().setSource(new ExternalResource(channel.getSnippet().getThumbnails().getMedium().getUrl()));
            widget.getTitle().setCaption(channel.getSnippet().getTitle());
            widget.getDate().setValue(channel.getStatistics().getSubscriberCount().toString() + " abonnés");
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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
        mainDisplay = widget;
        return refresh();
    }
}
