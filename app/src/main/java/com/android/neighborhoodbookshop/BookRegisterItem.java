package com.android.neighborhoodbookshop;

public class BookRegisterItem {

    private String userId;
    private String book_register_isbn;
    private String book_register_image_path;
    private String book_register_title;
    private String book_register_writer;

   private String book_register_company;
   private String book_register_date;
   private String book_register_rateNum;
   private String book_register_rateMemo;


    //userId를 인자로 넣기 => 같은 책에 대해 여러 유저가 리뷰를 작성할수도 있으니
    public BookRegisterItem(String userId, String book_register_image_path, String book_register_title, String book_register_writer, String book_register_company, String book_register_date, String book_register_isbn, String book_register_rateNum, String book_register_rateMemo) {
        this.userId = userId;
        this.book_register_image_path = book_register_image_path;
        this.book_register_title = book_register_title;
        this.book_register_writer = book_register_writer;
        this.book_register_company = book_register_company;
        this.book_register_date = book_register_date;
        this.book_register_isbn = book_register_isbn;
        this.book_register_rateNum = book_register_rateNum;
        this.book_register_rateMemo = book_register_rateMemo;
    }

    public String getBook_register_image_path() {
        return book_register_image_path;
    }

    public void setBook_register_image_path(String book_register_image_path) {
        this.book_register_image_path = book_register_image_path;
    }

    public String getBook_register_title() {
        return book_register_title;
    }

    public void setBook_register_title(String book_register_title) {
        this.book_register_title = book_register_title;
    }

    public String getBook_register_writer() {
        return book_register_writer;
    }

    public void setBook_register_writer(String book_register_writer) {
        this.book_register_writer = book_register_writer;
    }

    public String getBook_register_company() {
        return book_register_company;
    }

    public void setBook_register_company(String book_register_company) {
        this.book_register_company = book_register_company;
    }

    public String getBook_register_date() {
        return book_register_date;
    }

    public void setBook_register_date(String book_register_date) {
        this.book_register_date = book_register_date;
    }


    public String getBook_register_rateNum() {
        return book_register_rateNum;
    }

    public void setBook_register_rateNum(String book_register_rateNum) {
        this.book_register_rateNum = book_register_rateNum;
    }

    public String getBook_register_rateMemo() {
        return book_register_rateMemo;
    }

    public void setBook_register_rateMemo(String book_register_rateMemo) {
        this.book_register_rateMemo = book_register_rateMemo;
    }

    public String getBook_register_isbn() {
        return book_register_isbn;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setBook_register_isbn(String book_register_isbn) {
        this.book_register_isbn = book_register_isbn;
    }
}
