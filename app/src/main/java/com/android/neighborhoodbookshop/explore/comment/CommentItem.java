package com.android.neighborhoodbookshop.explore.comment;

public class CommentItem {
    String userImagePath;
    String userName;
    String userLocation;
    String comment;
    long timestamp; //아이템이 작성된 시간


    public CommentItem(String userImagePath, String userName, String userLocation, String comment, long timestamp) {
        this.userImagePath = userImagePath;
        this.userName = userName;
        this.userLocation = userLocation;
        this.comment = comment;
        this.timestamp = timestamp;
    }

    public String getUserImagePath() {
        return userImagePath;
    }

    public void setUserImagePath(String userImagePath) {
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

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
