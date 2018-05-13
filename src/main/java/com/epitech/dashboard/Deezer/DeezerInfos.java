package com.epitech.dashboard.Deezer;

import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class DeezerInfos {
    private static final String urlSearchName = "https://api.deezer.com/search?q=";
    private static final String urlSearchId = "https://api.deezer.com/artist/";

    public String searchArtistByName(String nameArtist)
    {
        String artistId = null;
        nameArtist = nameArtist.trim();
        String url = urlSearchName + nameArtist;

        RestTemplate template = new RestTemplate();
        LinkedHashMap map = template.getForObject(url, LinkedHashMap.class);

        try {
            LinkedHashMap entry = ((ArrayList<LinkedHashMap>)map.get("data")).get(0);
            artistId = String.valueOf((int)((LinkedHashMap) entry.get("artist")).get("id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return artistId;
    }

    public String searchArtistById(String artistId)
    {
        String artistFans = null;
        String url = urlSearchId + artistId;

        RestTemplate template = new RestTemplate();
        LinkedHashMap map = template.getForObject(url, LinkedHashMap.class);

        try {
            artistFans = String.valueOf((int) map.get("nb_fan"));
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return artistFans;
    }

    public String getUrlPicture(String artistId)
    {
        String urlImage = null;
        String url = urlSearchId + artistId;

        RestTemplate template = new RestTemplate();
        LinkedHashMap map = template.getForObject(url, LinkedHashMap.class);

        try {
            urlImage = map.get("picture_medium").toString();
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return urlImage;
    }

    public String getArtistName(String artistId)
    {
        String artistName = null;
        String url = urlSearchId + artistId;

        RestTemplate template = new RestTemplate();
        LinkedHashMap map = template.getForObject(url, LinkedHashMap.class);

        try {
            artistName = map.get("name").toString();
        } catch (RestClientException e) {
            e.printStackTrace();
        }

        return artistName;
    }

    public String getArtistNbAlbums(String artistId)
    {
        String nbAlbums = null;
        String url = urlSearchId + artistId;

        RestTemplate template = new RestTemplate();
        LinkedHashMap map = template.getForObject(url, LinkedHashMap.class);

        try {
            nbAlbums = map.get("nb_album").toString();
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return nbAlbums;
    }
}
