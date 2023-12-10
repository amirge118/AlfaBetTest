package org.example.request;

import java.util.Date;

public class EventRequest {

    private String name;
    private Date date;
    private int popularity;
    private final String locationName;

    public EventRequest(String name, Date date, int popularity, String locationName) {
        this.name = name;
        this.date = date;
        this.popularity = popularity;
        this.locationName = locationName;
    }

    public String getName() {
        return name;
    }
    public Date getDate() {
        return date;
    }
    public int getPopularity() {
        return popularity;
    }
    public String getLocationName() {
        return locationName;
    }
}
