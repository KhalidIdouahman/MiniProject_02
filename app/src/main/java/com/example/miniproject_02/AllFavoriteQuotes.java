package com.example.miniproject_02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
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
    SharedPreferences layoutSession;
    ArrayList<RecyclerView.LayoutManager> layoutsList = new ArrayList<>();
    int layoutId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favoriteBindingViews = ActivityAllFavoriteQuotesBinding.inflate(getLayoutInflater());
        root = favoriteBindingViews.getRoot();
        setContentView(root);

        layoutSession = getSharedPreferences("layout_session" , MODE_PRIVATE);

        layoutId = layoutSession.getInt("layout_id" , 0);

        layoutsList.add(new LinearLayoutManager(this));
        layoutsList.add(new GridLayoutManager(this , 2));

        favoriteQuotesDB = new FavoriteQuotesSQLiteDB(this);
        listOfFavQuotes = favoriteQuotesDB.getAllQuotes();
        favoriteQuotesAdapter = new FavoriteQuotesAdapter(this , listOfFavQuotes);
        favoriteBindingViews.allQuotesRecycler.setLayoutManager(layoutsList.get(layoutId));
        favoriteBindingViews.allQuotesRecycler.setAdapter(favoriteQuotesAdapter);

        registerForContextMenu(favoriteBindingViews.changeLayoutBtn);

    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderTitle("Layout Type");
        menu.add(0 , 0 , 0 , "List");
        menu.add(0 , 1 , 0 , "Grid");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        SharedPreferences.Editor edit = layoutSession.edit();

        if (item.getItemId() == 0) {
            favoriteBindingViews.allQuotesRecycler.setLayoutManager(layoutsList.get(0));
            edit.putInt("layout_id" , 0);
        } else {
            favoriteBindingViews.allQuotesRecycler.setLayoutManager(layoutsList.get(1));
            edit.putInt("layout_id" , 1);
        }

        edit.apply();

        return super.onContextItemSelected(item);
    }

}