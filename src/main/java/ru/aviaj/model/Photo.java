package ru.aviaj.model;

public class Photo {

    private long placeId;
    private String url;

    public Photo(long placeId, String url) {
        this.placeId = placeId;
        this.url = url;
    }

    public long getPlaceId() {
        return placeId;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
