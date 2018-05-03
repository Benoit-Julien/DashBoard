package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.RSSFeed.RSSWidgetLayout;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.*;

import java.net.MalformedURLException;
import java.net.URL;

public class RSSFeedWidget extends AWidget {
    private TextField urlField;

    private RSSWidgetLayout widget;

    private URL currentRssFeed;

    public RSSFeedWidget(int uid) {
        super(uid, "RSS Feed");

        urlField = new TextField("Rss url : ");

        formContent.addComponent(urlField);
        widget = new RSSWidgetLayout();
    }

    private RSSFeedWidget(AWidget source)
    {
        super(source);
        if (source instanceof RSSFeedWidget) {
            urlField = ((RSSFeedWidget) source).urlField;
            currentRssFeed = ((RSSFeedWidget) source).currentRssFeed;
            widget = new RSSWidgetLayout();
        } else
            throw new IllegalArgumentException(CLONE_ERR);
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
            widget.getDate().setValue(current.getPublishedDate().toString());

            if (!current.getEnclosures().isEmpty() && current.getEnclosures().get(0).getType().contains("image"))
                widget.getImage().setSource(new ExternalResource(current.getEnclosures().get(0).getUrl()));
            else
                widget.getImage().setEnabled(false);

            if (current.getDescription().getType().contains("text"))
                widget.getDesc().setValue(current.getDescription().getValue());
            else
                widget.getDesc().setEnabled(false);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ERROR: "+ex.getMessage());
        }
    }

    @Override
    public AWidget clone() {
        return new RSSFeedWidget(this);
    }

    @Override
    public boolean submitted() {
        try {
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
