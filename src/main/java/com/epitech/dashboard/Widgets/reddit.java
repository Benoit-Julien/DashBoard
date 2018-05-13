package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.Widget;
import com.epitech.dashboard.youtube.VideoLayout;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.TextField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


public class reddit extends AWidget{
    private String subreddit = null;
    private String Url = "https://www.reddit.com/r/";
    private String minimal = "";
    private String Title = "";
    private TextField SubField = new TextField();

    private VideoLayout widget = new VideoLayout();
    public reddit() {
        formContent.addComponent(SubField);
    }

    private String build_sub() {
        if (SubField.getValue() != "")
            return (Url.concat(SubField.getValue()).concat("/"));
        else if(subreddit != null)
            return (Url.concat(subreddit).concat("/"));
        else
            return "";
    }

    @Override
    public boolean refresh() {
        try {

            Document doc = Jsoup.connect(build_sub()).get();
            String Webpage = doc.body().toString();

            String[] first;
            String UrlImg = " ";
            //System.out.print(WebPage);
            try {
                first = Webpage.split("<div id=\"siteTable\"");
                String[] sec = first[1].split("<img src=\"");
                String[] third = sec[1].split("\"");
                UrlImg = third[0];
            } catch (Exception e) {
                e.printStackTrace();
                //return false;
            }
            first = Webpage.split("<div id=\"siteTable\"");
            String[] fourth = first[1].split("<a class=\"");
            String[] fifth = fourth[1].split("href=\"");
            String[] sixth = fifth[1].split("\"");
            String UrlTitle = sixth[0];

            widget.getThumbnail().setSource(new ExternalResource(UrlImg));
            widget.getTitle().setCaption(subreddit);
            widget.getTitle().setResource(new ExternalResource("https://www.reddit.com/" + UrlTitle));
            widget.getDate().setValue(" ");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void loadFromData(Widget source) {
        String data = (String) source.getInstance();
        subreddit = data;
        submitted();
    }

    @Override
    public Widget SaveWidget() {
        Widget save = new Widget();
        save.setInstance(subreddit);
        save.setType(this.getClass().getName());
        return save;
    }

    @Override
    public boolean submitted() {
        if (subreddit == null)
            subreddit = SubField.getValue();
        mainDisplay = widget;
        return refresh();
    }
}
