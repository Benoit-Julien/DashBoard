package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.Widget;
import com.epitech.dashboard.youtube.LastVideoWidgetLayout;
import com.epitech.dashboard.youtube.VideoLayout;
import com.epitech.dashboard.youtube.YoutubeRequests;
import com.google.api.services.youtube.model.Channel;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.TextField;

public class ViewsNumber extends AWidget {
    private VideoLayout widget = new VideoLayout();
    private YoutubeRequests request = new YoutubeRequests();
    private TextField idField = new TextField("Lien de la chaine youtube");
    private String urlChannel = "";

    public ViewsNumber() {
        formContent.addComponent(idField);
    }

    @Override
    public boolean refresh() {
        Channel channel = request.findChannel(urlChannel).getItems().get(0);
        try {
            widget.getThumbnail().setSource(new ExternalResource(channel.getSnippet().getThumbnails().getMedium().getUrl()));
            widget.getTitle().setCaption(channel.getSnippet().getTitle());
            widget.getDate().setValue("Nombre de vues: ".concat(channel.getStatistics().getViewCount().toString()));
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
