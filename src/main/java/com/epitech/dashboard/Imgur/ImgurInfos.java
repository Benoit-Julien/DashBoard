package com.epitech.dashboard.Imgur;

public class ImgurInfos {
    private String tagName;

    public ImgurInfos() {

    }

    public ImgurInfos(String tagName) {
        this.tagName = tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }
}