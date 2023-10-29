package com.android.neighborhoodbookshop;

public class CommentItem {
    String userImagePath;
    String userName;
    String userLocation;
    String comment;
    String time;

    public CommentItem(String userImagePath, String userName, String userLocation, String comment, String time) {
        this.userImagePath = userImagePath;
        this.userName = userName;
        this.userLocation = userLocation;
        this.comment = comment;
        this.time = time;
    }


    public String getUserImagePath() {
        return userImagePath;
    }

    public void seUserImagePath(String imagePath) {
        this.userImagePath = userImagePath;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(String userLocation) {
        this.userLocation = userLocation;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
