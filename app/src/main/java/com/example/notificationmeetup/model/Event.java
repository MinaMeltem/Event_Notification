package com.example.notificationmeetup.model;

/**
 * Created by meltemyildirim on 4/18/17.
 */


/**
 * This is data model class that corresponds fields in meetup Api endpoint, which is self/events
 */
public class Event {

    private String name;
    private long time;
    private String link;


    public String getName() {
        return name;
    }

    public long getTime() {
        return time;
    }

    public String getLink() {
        return link;
    }
}
