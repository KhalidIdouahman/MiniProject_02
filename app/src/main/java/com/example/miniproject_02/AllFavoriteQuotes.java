package com.example.miniproject_02;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.example.miniproject_02.Models.Quote;
import com.example.miniproject_02.databinding.ActivityAllFavoriteQuotesBinding;
import com.example.miniproject_02.db.FavoriteQuotesSQLiteDB;

import java.util.ArrayList;

public class AllFavoriteQuotes extends AppCompatActivity {
    ActivityAllFavoriteQuotesBinding favoriteBindingViews;
    View root;
    FavoriteQuotesAdapter favoriteQuotesAdapter;
    ArrayList<Quote> listOfFavQuotes;
    FavoriteQuotesSQLiteDB favoriteQuotesDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteBindingViews = ActivityAllFavoriteQuotesBinding.inflate(getLayoutInflater());
        root = favoriteBindingViews.getRoot();
        setContentView(root);

        favoriteQuotesDB = new FavoriteQuotesSQLiteDB(this);
        listOfFavQuotes = favoriteQuotesDB.getAllQuotes();
        favoriteQuotesAdapter = new FavoriteQuotesAdapter(this , listOfFavQuotes);
        favoriteBindingViews.allQuotesRecycler.setLayoutManager(new LinearLayoutManager(this));
        favoriteBindingViews.allQuotesRecycler.setAdapter(favoriteQuotesAdapter);
    }
}