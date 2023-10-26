package com.android.neighborhoodbookshop;

public class BookReviewItem {

    String key;
    String bookName;
    String book_imagePath;
    String writer;
    String profile_imagePath;
    String profile_name;
    String profile_location;

    public BookReviewItem(String key, String bookName, String book_imagePath, String writer, String profile_imagePath, String profile_name, String profile_location) {
        this.key = key;
        this.bookName = bookName;
        this.book_imagePath = book_imagePath;
        this.writer = writer;
        this.profile_imagePath = profile_imagePath;
        this.profile_name = profile_name;
        this.profile_location = profile_location;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBook_imagePath() {
        return book_imagePath;
    }

    public void setBook_imagePath(String book_imagePath) {
        this.book_imagePath = book_imagePath;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getProfile_imagePath() {
        return profile_imagePath;
    }

    public void setProfile_imagePath(String profile_imagePath) {
        this.profile_imagePath = profile_imagePath;
    }

    public String getProfile_name() {
        return profile_name;
    }

    public void setProfile_name(String profile_name) {
        this.profile_name = profile_name;
    }

    public String getProfile_location() {
        return profile_location;
    }

    public void setProfile_location(String profile_location) {
        this.profile_location = profile_location;
    }
}
