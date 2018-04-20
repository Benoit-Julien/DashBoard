package com.epitech.dashboard.youtube;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class YoutubeRequests {

    private static final String API_KEY = "AIzaSyDNNiVF69c_TfUPTBk598pu_E-q4RsZn4g";

    /**
     * Define a global instance of the HTTP transport.
     */
    public static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    /**
     * Define a global instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = new JacksonFactory();

    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    private static YouTube youtube;

    public YoutubeRequests(){
        youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).setApplicationName("dashboard-last-channel-video").build();
    }

    public ChannelListResponse findChannel(String url){
        List<String> elems = Arrays.asList(url.split("/"));
        String end = "";
        String type = "";
        boolean build = false;
        for (String elem: elems){
            if (build)
                end = end.concat(elem);
            if (elem.equals("channel") || elem.equals("user"))
            {
                build = true;
                type = elem;
            }
        }
        if (type.equals("channel"))
            return findChannelById(end);
        if (type.equals("user"))
            return findChannelByUser(end);
        return null;
    }

    private ChannelListResponse findChannelById(String id){
        try {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("part", "snippet,contentDetails,statistics");
            parameters.put("key", API_KEY);
            parameters.put("id", id);

            YouTube.Channels.List channelsListByIdRequest = youtube.channels().list(parameters.get("part"));
            channelsListByIdRequest.setKey(parameters.get("key"));
            if (parameters.containsKey("id") && !parameters.get("id").equals("")) {
                channelsListByIdRequest.setId(parameters.get("id"));
            }

            return channelsListByIdRequest.execute();
        }catch (IOException ex)
        {
            System.out.println(ex.toString());
            return null;
        }
    }

    private ChannelListResponse findChannelByUser(String user){
        try {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("part", "snippet,contentDetails,statistics");
            parameters.put("key", API_KEY);
            parameters.put("forUsername", user);

            YouTube.Channels.List channelsListByIdRequest = youtube.channels().list(parameters.get("part"));
            channelsListByIdRequest.setKey(parameters.get("key"));
            if (parameters.containsKey("forUsername") && !parameters.get("forUsername").equals("")) {
                channelsListByIdRequest.setForUsername(parameters.get("forUsername"));
            }
            return channelsListByIdRequest.execute();
        }catch (IOException ex)
        {
            System.out.println(ex.toString());
            return null;
        }
    }
}
