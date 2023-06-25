package com.example.miniproject_02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

        registerForContextMenu(favoriteBindingViews.changeLayoutBtn);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Layout Type");
        menu.add(0 , v.getId() , 0 , "List");
        menu.add(0 , v.getId() , 0 , "Grid");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if (item.getTitle().equals("List")) {
            favoriteBindingViews.allQuotesRecycler.setLayoutManager(new LinearLayoutManager(this));
        } else {
            favoriteBindingViews.allQuotesRecycler.setLayoutManager(new GridLayoutManager(this , 2));
        }
        return super.onContextItemSelected(item);
    }

}