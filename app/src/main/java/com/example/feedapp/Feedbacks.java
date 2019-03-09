package com.example.feedapp;

public class Feedbacks {

    public String feed,createdby;

    public Feedbacks()
    {

    }

    public Feedbacks(String feed, String createdby) {
        this.feed = feed;
        this.createdby = createdby;
    }

    public String getFeed() {
        return feed;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setFeed(String feed) {
        this.feed = feed;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }
}
