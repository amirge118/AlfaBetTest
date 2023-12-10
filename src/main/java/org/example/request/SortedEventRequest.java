package org.example.request;

public class SortedEventRequest {

    private final String sortBy;
    private final String location;
    public SortedEventRequest(String sortBy, String location) {
        this.sortBy = sortBy;
        this.location = location;
    }

    public String getLocation() {
        return location;
    }
    public String getSortBy() {
        return sortBy;
    }
}
