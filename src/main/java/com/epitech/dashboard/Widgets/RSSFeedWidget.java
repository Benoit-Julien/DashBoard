package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.RSSFeed.RSSWidgetLayout;
import com.epitech.dashboard.Widget;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.TextField;

import java.net.MalformedURLException;
import java.net.URL;

public class RSSFeedWidget extends AWidget {
    private TextField urlField = new TextField("Rss url : ");

    private RSSWidgetLayout widget = new RSSWidgetLayout();

    private URL currentRssFeed;

    public RSSFeedWidget() {
        formContent.addComponent(urlField);
    }

    @Override
    public void refresh() {
        try {
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(currentRssFeed));

            if (feed.getEntries().isEmpty())
                return;

            SyndEntry current = feed.getEntries().get(0);

            widget.getTitle().setCaption(current.getTitle());
            widget.getTitle().setResource(new ExternalResource(current.getLink()));
            widget.getTitle().setTargetName("_blank");
            if (current.getPublishedDate() != null)
                widget.getDate().setValue(current.getPublishedDate().toString());
            else
                widget.getDate().setEnabled(false);

            if (current.getEnclosures() != null
                    && !current.getEnclosures().isEmpty()
                    && current.getEnclosures().get(0).getType().contains("image"))
                widget.getImage().setSource(new ExternalResource(current.getEnclosures().get(0).getUrl()));
            else
                widget.getImage().setEnabled(false);

            if (current.getDescription().getType().contains("text"))
                widget.getDesc().setValue(current.getDescription().getValue());
            else
                widget.getDesc().setEnabled(false);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: " + ex.getMessage());
        }
    }

    @Override
    public void loadFromData(Widget source) {
        try {
            currentRssFeed = new URL((String) source.getInstance());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        submitted();
    }

    @Override
    public Widget SaveWidget() {
        Widget save = new Widget();
        save.setInstance(currentRssFeed.toString());
        save.setType(this.getClass().getName());
        return save;
    }

    @Override
    public boolean submitted() {
        try {
            if (currentRssFeed == null)
                currentRssFeed = new URL(urlField.getValue());
            //currentRssFeed = new URL("http://www.lemonde.fr/rss/une.xml");
            mainDisplay = widget;
            refresh();
            return true;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
