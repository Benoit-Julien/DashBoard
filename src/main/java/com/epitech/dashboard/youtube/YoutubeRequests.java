package com.epitech.dashboard.youtube;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.PlaylistItemListResponse;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

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
        String[] elems = url.split("/");
        String end = "";
        String type = "";
        boolean build = false;
        for (String elem: elems){
            if (build) {
                end = elem;
                break;
            }
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

    /**
     * Gets the channel info based on his id
     * @param id Id of the youtube account
     * @return Response of the query
     */
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

    /**
     * Gets the channel info based on his username
     * @param user Username of the youtube account
     * @return Response of the query
     */
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

    public PlaylistItem getChannelLastVideo(Channel channel){
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("key", API_KEY);
        parameters.put("part", "snippet,contentDetails");
        parameters.put("maxResults", "5");
        parameters.put("playlistId", channel.getContentDetails().getRelatedPlaylists().getUploads());
        try {
            YouTube.PlaylistItems.List playlistRequest = youtube.playlistItems().list(parameters.get("part"));
            playlistRequest.setKey(parameters.get("key"));
            playlistRequest.setPart(parameters.get("part"));
            playlistRequest.setPlaylistId(parameters.get("playlistId"));
            playlistRequest.setMaxResults(Long.parseLong(parameters.get("maxResults")));
            PlaylistItemListResponse response = playlistRequest.execute();

            if (!response.getItems().isEmpty()) {
                response.getItems().sort((o1, o2) -> {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
                    try {
                        Date date1 = sdf.parse(o1.getSnippet().getPublishedAt().toStringRfc3339());
                        Date date2 = sdf.parse(o2.getSnippet().getPublishedAt().toStringRfc3339());
                        return date1.compareTo(date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return 0;
                });
                return response.getItems().get(response.getItems().size() - 1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
