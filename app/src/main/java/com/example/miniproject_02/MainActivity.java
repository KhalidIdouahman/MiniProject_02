package com.example.miniproject_02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.miniproject_02.Models.Quote;
import com.example.miniproject_02.databinding.ActivityMainBinding;
import com.example.miniproject_02.db.FavoriteQuotesSQLiteDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding bindingViews;
    View root;
    SharedPreferences session;
    boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingViews = ActivityMainBinding.inflate(getLayoutInflater());
        root = bindingViews.getRoot();
        setContentView(root);

        //region Pin Quote with SharedPreferences
        session = getSharedPreferences("pin_the_quotes" , MODE_PRIVATE);

        String quote = session.getString("quote" , null);

        if (quote == null) {
            sendRequestToGetQuotes();
        } else {
            int id = session.getInt("id" , 0);
            String author = session.getString("author" , null);

            bindingViews.idTv.setText(String.valueOf(id));
            bindingViews.quoteTv.setText(quote);
            bindingViews.authorTv.setText(author);

            bindingViews.pinToggleBtn.setChecked(true);
        }

        bindingViews.pinToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editSession = session.edit();
                int savedId = 0;
                String savedQuote = null;
                String savedAuthor = null;

                if (isChecked) {
                    savedId = Integer.parseInt(bindingViews.idTv.getText().toString());
                    savedQuote = bindingViews.quoteTv.getText().toString();
                    savedAuthor = bindingViews.authorTv.getText().toString();
                } else {
                    sendRequestToGetQuotes();
                    isFavorite = false;
                    bindingViews.isFavoriteIm.setBackgroundResource(R.drawable.ic_unfavorite);
                }

                editSession.putInt("id" , savedId);
                editSession.putString("quote" , savedQuote);
                editSession.putString("author" , savedAuthor);

                editSession.apply();
            }
        });
        //endregion

        //region favorite the quotes and save them into Sqlite.

        FavoriteQuotesSQLiteDB db = new FavoriteQuotesSQLiteDB(this);

        bindingViews.isFavoriteIm.setOnClickListener(v -> {
            int id = Integer.parseInt(bindingViews.idTv.getText().toString());
            String quoteSqlite = bindingViews.quoteTv.getText().toString();
            String authorSqlite = bindingViews.authorTv.getText().toString();

            if (isFavorite) {
                bindingViews.isFavoriteIm.setBackgroundResource(R.drawable.ic_unfavorite);
                db.deleteQuote(id);
            } else {
                bindingViews.isFavoriteIm.setBackgroundResource(R.drawable.ic_favorite);
                db.addQuote(new Quote(id,quoteSqlite,authorSqlite));
            }

            for (Quote quoteSaved : db.getAllQuotes()) {
                Log.e("Sql quotes", quoteSaved.toString());
            }
        });
        //endregion

        bindingViews.showAllQuotesBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AllFavoriteQuotes.class);
            startActivity(intent);
        });
    }

    private void sendRequestToGetQuotes() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = getRandomUrls(25 , 80);

        JsonObjectRequest JsonObjReq = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    bindingViews.idTv.setText(response.getString("id"));
                    bindingViews.quoteTv.setText(response.getString("quote"));
                    bindingViews.authorTv.setText(response.getString("author"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "There is some error !!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(JsonObjReq);
    }

    public String getRandomUrls(int min , int max) {
        Random random = new Random();
        int randomInt = random.nextInt(max - min + 1) + min;
        return "https://dummyjson.com/quotes/" + randomInt;
    }
}