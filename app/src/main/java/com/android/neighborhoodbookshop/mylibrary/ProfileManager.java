package com.android.neighborhoodbookshop.mylibrary;

public class ProfileManager {

    private String imagePath;
    private String location;
    private String introduction;
    private String instaURL;

    private double lat; //사용자 위치 위도
    private double lng; //사용자 위치 경도


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getInstaURL() {
        return instaURL;
    }

    public void setInstaURL(String instaURL) {
        this.instaURL = instaURL;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
