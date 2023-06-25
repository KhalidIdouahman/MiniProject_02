package com.example.miniproject_02.Models;

import androidx.annotation.NonNull;

public class Quote {
    private int id;
    private String quote;
    private String author;

    public Quote(int id, String quote, String author) {
        this.id = id;
        this.quote = quote;
        this.author = author;
    }

    public int getId() {
        return id;
    }

    public String getQuote() {
        return quote;
    }

    public String getAuthor() {
        return author;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("The Quote : %d , %s , %s ." , getId() , getQuote(), getAuthor());
    }
}
