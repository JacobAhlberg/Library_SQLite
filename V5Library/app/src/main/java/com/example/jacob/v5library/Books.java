package com.example.jacob.v5library;

/**
 * Created by Jacob on 2017-02-14.
 */

public class Books{
    private long book_id;
    private String title;
    private String author;

    public long getBook_id() {
        return book_id;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setBook_id(long book_id) {
        this.book_id = book_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return title;
    }

}
