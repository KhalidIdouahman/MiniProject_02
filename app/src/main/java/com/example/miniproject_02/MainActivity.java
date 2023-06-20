package com.example.miniproject_02;

import androidx.appcompat.app.AppCompatActivity;

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
            String author = session.getString("author" , null);

            bindingViews.quoteTv.setText(quote);
            bindingViews.authorTv.setText(author);

            bindingViews.pinToggleBtn.setChecked(true);
        }

        bindingViews.pinToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editSession = session.edit();
                String savedQuote = null;
                String savedAuthor = null;

                if (isChecked) {
                    savedQuote = bindingViews.quoteTv.getText().toString();
                    savedAuthor = bindingViews.authorTv.getText().toString();
                } else {
                    sendRequestToGetQuotes();
                }

                editSession.putString("quote" , savedQuote);
                editSession.putString("author" , savedAuthor);

                editSession.apply();
            }
        });
        //endregion

        // for test and it's working .
        FavoriteQuotesSQLiteDB db = new FavoriteQuotesSQLiteDB(this);

//        db.addQuote(1, "khalid is the best !" , "Khalid");
//        db.addQuote(2 , "if you have a dream that's all what you need ." , "me");
//        db.addQuote(3, "to delete" , "bey");

//        db.getAll();

        db.addQuote(new Quote(1, "khalid is the best !" , "Khalid"));
        db.addQuote(new Quote(2 , "if you have a dream that's all what you need ." , "me"));
        db.addQuote(new Quote(3, "to delete" , "bey"));

        db.deleteQuote(3);

        for (Quote quoteSaved : db.getAllQuotes()) {
            Log.e("Sql quotes", String.format("getAll: %d , %s , %s ." , quoteSaved.getId() , quoteSaved.getQuote(), quoteSaved.getAuthor()));
        }
        //

        bindingViews.passBtn.setOnClickListener(v -> {
            finish();
        });
    }

    private void sendRequestToGetQuotes() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = getRandomUrls(25 , 80);

        JsonObjectRequest JsonObjReq = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    bindingViews.quoteTv.setText(response.getString("quote"));
                    bindingViews.authorTv.setText(response.getString("author"));
                    Toast.makeText(MainActivity.this, response.getString("id"), Toast.LENGTH_SHORT).show();
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