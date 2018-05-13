package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.Deezer.DeezerInfos;
import com.epitech.dashboard.Deezer.DeezerWidgetLayout;
import com.epitech.dashboard.Widget;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.TextField;

public class NumberAlbums extends AWidget{

    private TextField artist = new TextField("Artiste : ");

    private DeezerInfos deezerInfos = new DeezerInfos();

    private DeezerWidgetLayout widget = new DeezerWidgetLayout();


    private String idArtist = null;

    private String nbAlbums;

    public NumberAlbums() {
        formContent.addComponent(artist);
    }



    @Override
    public boolean refresh() {
        try {
            nbAlbums = deezerInfos.getArtistNbAlbums(idArtist);
            widget.getImage().setSource(new ExternalResource(deezerInfos.getUrlPicture(idArtist)));
            widget.getInformations().setValue(deezerInfos.getArtistName(idArtist) + " a publié " + nbAlbums + " albums");

        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void loadFromData(Widget source) {
        idArtist = (String) source.getInstance();
        submitted();
    }

    @Override
    public Widget SaveWidget() {
        Widget save = new Widget();
        save.setInstance(idArtist);
        save.setType(this.getClass().getName());
        return save;
    }

    @Override
    public boolean submitted() {
            if (idArtist == null)
                idArtist = deezerInfos.searchArtistByName(artist.getValue());
            mainDisplay = widget;
            return refresh();
    }
}
