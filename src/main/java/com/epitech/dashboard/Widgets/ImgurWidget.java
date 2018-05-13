package com.epitech.dashboard.Widgets;

import com.epitech.dashboard.Imgur.ImgurInfos;
import com.epitech.dashboard.Imgur.ImgurWidgetLayout;
import com.epitech.dashboard.Widget;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.server.ExternalResource;
import com.vaadin.ui.ComboBox;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ImgurWidget extends AWidget {
    private static final String CLIENT_ID = "Client-ID c8e3f95212d91be";
    private static ArrayList<String> tagsList;

    private ImgurInfos infos = new ImgurInfos();

    private ImgurWidgetLayout widget = new ImgurWidgetLayout();

    private ComboBox<String> tagSelect = new ComboBox<>("Sélectionner un tag : ");

    public ImgurWidget() {
        if (tagsList == null) {
            tagsList = new ArrayList<>();
            try {
                URL tags_request = new URL("https://api.imgur.com/3/tags");
                HttpURLConnection connection = (HttpURLConnection) tags_request.openConnection();

                connection.setRequestMethod("GET");
                connection.setRequestProperty("Authorization", CLIENT_ID);

                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JsonObject json = new JsonParser().parse(response.toString()).getAsJsonObject();

                JsonArray tags = json.get("data").getAsJsonObject().get("tags").getAsJsonArray();

                for (JsonElement tag : tags) {
                    String name = tag.getAsJsonObject().get("name").getAsString();

                    name = name.replace('_', ' ');
                    name = name.substring(0, 1).toUpperCase() + name.substring(1);
                    tagsList.add(name);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        tagSelect.setDataProvider(new ListDataProvider<>(tagsList));
        tagSelect.setEmptySelectionAllowed(false);
        formContent.addComponent(tagSelect);
    }

    @Override
    public boolean refresh() {
        JsonObject item;
        try {
            URL image_request = new URL("https://api.imgur.com/3/gallery/t/" + infos.getTagName());
            HttpURLConnection connection = (HttpURLConnection) image_request.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Authorization", CLIENT_ID);

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            JsonObject json = new JsonParser().parse(response.toString()).getAsJsonObject();
            item = json.get("data").getAsJsonObject().get("items").getAsJsonArray().get(0).getAsJsonObject();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        JsonObject image;

        if (item.has("layout")) {
            if (!item.get("layout").getAsString().equals("blog"))
                return false;
            image = item.get("images").getAsJsonArray().get(0).getAsJsonObject();
        } else
            image = item;

        String type = image.get("type").getAsString();
        String link = image.get("link").getAsString();

        try {
            if (type.contains("image")) {
                widget.getVideo().setVisible(false);
                widget.getImage().setSource(new ExternalResource(new URL(link)));
            } else if (type.contains("video")) {
                widget.getImage().setVisible(false);
                widget.getVideo().setSource(new ExternalResource(new URL(link)));
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void loadFromData(Widget source) {
        ObjectMapper mapper = new ObjectMapper();
        infos = mapper.convertValue(source.getInstance(), ImgurInfos.class);
        submitted();
    }

    @Override
    public Widget SaveWidget() {
        Widget save = new Widget();
        save.setInstance(infos);
        save.setType(this.getClass().getName());
        return save;
    }

    @Override
    public boolean submitted() {
        if (infos.getTagName() == null) {
            if (tagSelect.isEmpty())
                return false;
            infos.setTagName(tagSelect.getValue());
        }

        mainDisplay = widget;
        return refresh();
    }
}
