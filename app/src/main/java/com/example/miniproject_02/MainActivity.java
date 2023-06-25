package com.example.miniproject_02;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.miniproject_02.Models.ColorModel;
import com.example.miniproject_02.Models.Quote;
import com.example.miniproject_02.Models.Settings;
import com.example.miniproject_02.databinding.ActivityMainBinding;
import com.example.miniproject_02.db.FavoriteQuotesSQLiteDB;
import com.example.miniproject_02.db.SettingsSQLiteDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding bindingViews;
    View root;
    FavoriteQuotesSQLiteDB db;
    boolean isFavorite ;
    SharedPreferences session , firstTime;
    SettingsSQLiteDB settingsSQLiteDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingViews = ActivityMainBinding.inflate(getLayoutInflater());
        root = bindingViews.getRoot();
        setContentView(root);

        db = new FavoriteQuotesSQLiteDB(this);

        settingsSQLiteDB = new SettingsSQLiteDB(this);

        //region this is for execute this code just for the first time the app is installed
        firstTime = getSharedPreferences("forFirstTime" , MODE_PRIVATE);

        boolean isFirstTime = firstTime.getBoolean("isFirstTime" , true);

        if (isFirstTime) {
            settingsSQLiteDB.addColor(new ColorModel("Default" , "#FFFFFF"));
            settingsSQLiteDB.addColor(new ColorModel("LightSalmon" , "#FFA07A"));
            settingsSQLiteDB.addColor(new ColorModel("Plum" , "#DDA0DD"));
            settingsSQLiteDB.addColor(new ColorModel("PaleGreen" , "#98FB98"));
            settingsSQLiteDB.addColor(new ColorModel("CornFlowerBlue" , "#6495ED"));

            settingsSQLiteDB.addBgColor();
            SharedPreferences.Editor edit = firstTime.edit();
            edit.putBoolean("isFirstTime" , false);
            edit.apply();
        }
        //endregion




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
                    if (!isFavorite) {
                        isFavorite = true;
                        bindingViews.isFavoriteIm.setBackgroundResource(R.drawable.ic_favorite);
                        db.addQuote(new Quote(savedId , savedQuote , savedAuthor));
                    }
                } else {
                    sendRequestToGetQuotes();
                }

                editSession.putInt("id" , savedId);
                editSession.putString("quote" , savedQuote);
                editSession.putString("author" , savedAuthor);

                editSession.apply();
            }
        });
        //endregion

        isFavQuoteInDB();

        bindingViews.refreshQuote.setOnClickListener(v -> {
            sendRequestToGetQuotes();
        });

        //region favorite the quotes and save them into Sqlite.

        bindingViews.isFavoriteIm.setOnClickListener(v -> {
            int idSqlite = Integer.parseInt(bindingViews.idTv.getText().toString());
            String quoteSqlite = bindingViews.quoteTv.getText().toString();
            String authorSqlite = bindingViews.authorTv.getText().toString();

            if (isFavorite) {
                if (bindingViews.pinToggleBtn.isChecked()) {
                    bindingViews.pinToggleBtn.setChecked(false);
                }
                bindingViews.isFavoriteIm.setBackgroundResource(R.drawable.ic_unfavorite);
                db.deleteQuote(idSqlite);
            } else {
                bindingViews.isFavoriteIm.setBackgroundResource(R.drawable.ic_favorite);
                db.addQuote(new Quote(idSqlite,quoteSqlite,authorSqlite));
            }

        });
        //endregion

        registerForContextMenu(bindingViews.colorSelectorIm);

        bindingViews.colorSelectorIm.setOnClickListener(v -> {
            openContextMenu(bindingViews.colorSelectorIm);
        });

        bindingViews.showAllQuotesBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, AllFavoriteQuotes.class);
            startActivity(intent);
        });

        ArrayList<ColorModel> colorModels = settingsSQLiteDB.getColors();

        String colorName = settingsSQLiteDB.getBgColor();
        int colorPosition = -1;
        for (int i = 0 ; i < colorModels.size() ; i++ ) {
            if (colorModels.get(i).getName().equals(colorName)) {
                colorPosition = i;
                break;
            }
        }

        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this , colorModels);
        bindingViews.selectBgColorSpinner.setAdapter(spinnerAdapter);
        bindingViews.selectBgColorSpinner.setSelection(colorPosition);

        bindingViews.selectBgColorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String colorName = colorModels.get(position).getName();
                String colorCode = colorModels.get(position).getCode();

                settingsSQLiteDB.updateBgColor(new Settings("bg_color" , colorName));
                root.setBackgroundColor(Color.parseColor(colorCode));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void isFavQuoteInDB() {
        int id = Integer.parseInt(bindingViews.idTv.getText().toString());
        if (db.isQuoteInDB(id)) {
            isFavorite = true;
            bindingViews.isFavoriteIm.setBackgroundResource(R.drawable.ic_favorite);
        } else {
            isFavorite = false;
            bindingViews.isFavoriteIm.setBackgroundResource(R.drawable.ic_unfavorite);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        String[] colorsNames = getResources().getStringArray(R.array.color_names);

        for (int i = 0; i < colorsNames.length; i++) {
            menu.add(0 , i , 0 , colorsNames[i]);
        }

    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        String[] colorsCodes = getResources().getStringArray(R.array.color_codes);

        int position = item.getItemId();
        String colorCode = colorsCodes[position];

        bindingViews.selectBgColorSpinner.setSelection(position);
        root.setBackgroundColor(Color.parseColor(colorCode));
        return true;
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
                    isFavQuoteInDB();
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