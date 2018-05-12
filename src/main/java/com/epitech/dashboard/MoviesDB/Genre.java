package com.epitech.dashboard.MoviesDB;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class Genre {
    private String id;
    private String name;

    public Genre(){

    }

    public Genre(String id, String name){
        this.name = name;
        this.id = id;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static List<Genre> GetGenres(List source){
        ArrayList<Genre> list = new ArrayList<>();

        for(Object entry: source){
            if (entry instanceof LinkedHashMap){
                ObjectMapper mapper = new ObjectMapper();
                try {
                    list.add(mapper.convertValue(entry, Genre.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return  list;
    }
}
