package com.example.miniproject_02.Models;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

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

    public Spannable designedInfos() {
        SpannableStringBuilder strQuote = new SpannableStringBuilder(getQuote());
        SpannableStringBuilder strSymbol = new SpannableStringBuilder("\"\" ");
        SpannableStringBuilder strSymbol1 = new SpannableStringBuilder(" \"\"");

        strSymbol.setSpan(new ForegroundColorSpan(Color.parseColor("#FFDD41")) , 0 , strSymbol.length()
                , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        strSymbol1.setSpan(new ForegroundColorSpan(Color.parseColor("#FFDD41")) , 0 , strSymbol.length()
                , Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        strQuote.setSpan(new ForegroundColorSpan(Color.parseColor("#434343")), 0 , strQuote.length(),
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        strQuote.insert(0 , strSymbol);
        strQuote.insert(strQuote.length() , strSymbol1);
        strQuote.insert(strQuote.length() , "\n\n" + getAuthor() + "\n");

        return strQuote;
    }
}
