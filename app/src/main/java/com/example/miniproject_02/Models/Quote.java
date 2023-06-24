package com.example.miniproject_02.Models;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.UnderlineSpan;

import androidx.annotation.NonNull;

import com.example.miniproject_02.RotatedImageSpan;

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

    public Spannable quoteDesign(Context context , int imageRes , int alignment) {
        SpannableStringBuilder strQuote = new SpannableStringBuilder(getQuote());

        strQuote.setSpan(new ForegroundColorSpan(Color.parseColor("#434343")), 0 , strQuote.length(),
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        strQuote.setSpan(new RelativeSizeSpan(2), 0 , 1,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

//        to set the text justify
        strQuote.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_NORMAL), 0 , 1,
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        strQuote.insert(0 , "  ");
        strQuote.insert(strQuote.length() , "  ");

        // Create a RotatedImageSpan instance with rotation of 180 degrees
        RotatedImageSpan imageSpan = new RotatedImageSpan(context, imageRes, alignment, 180);

        strQuote.setSpan(imageSpan , 0 , 1
                , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        strQuote.setSpan(new ImageSpan(context , imageRes , alignment) , strQuote.length() - 1 , strQuote.length()
                , Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return strQuote;
    }

    public Spannable authorDesign() {
        SpannableStringBuilder strAuthor = new SpannableStringBuilder(getAuthor());

        strAuthor.setSpan(new ForegroundColorSpan(Color.parseColor("#FFFFFF")), 0 , strAuthor.length(),
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        strAuthor.setSpan(new UnderlineSpan(), 0 , strAuthor.length(),
                Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        return strAuthor;
    }

}
