package com.android.neighborhoodbookshop;

public class BookClubItem {

    private String bookclub_image;
    private String bookclub_name;
    private String bookclub_online;
    private String bookclub_introduce;
    private int bookclub_category1;
    private int bookclub_category2;


    public BookClubItem( String bookclub_image, String bookclub_name, String bookclub_online, String bookclub_introduce, int bookclub_category1, int bookclub_category2) {
        this.bookclub_image = bookclub_image;
        this.bookclub_name = bookclub_name;
        this.bookclub_online = bookclub_online;
        this.bookclub_introduce = bookclub_introduce;
        this.bookclub_category1 = bookclub_category1;
        this.bookclub_category2 = bookclub_category2;
    }


    public String getBookclub_image() {
        return bookclub_image;
    }

    public void setBookclub_image( String bookclub_image) {
        this.bookclub_image = bookclub_image;
    }

    public String getBookclub_name() {
        return bookclub_name;
    }

    public void setBookclub_name(String bookclub_name) {
        this.bookclub_name = bookclub_name;
    }

    public String getBookclub_online() {
        return bookclub_online;
    }

    public void setBookclub_online(String bookclub_online) {
        this.bookclub_online = bookclub_online;
    }

    public String getBookclub_introduce() {
        return bookclub_introduce;
    }

    public void setBookclub_introduce(String bookclub_introduce) {
        this.bookclub_introduce = bookclub_introduce;
    }

    public int getBookclub_category1() {
        return bookclub_category1;
    }

    public void setBookclub_category1(int bookclub_category1) {
        this.bookclub_category1 = bookclub_category1;
    }

    public int getBookclub_category2() {
        return bookclub_category2;
    }

    public void setBookclub_category2(int bookclub_category2) {
        this.bookclub_category2 = bookclub_category2;
    }
}
