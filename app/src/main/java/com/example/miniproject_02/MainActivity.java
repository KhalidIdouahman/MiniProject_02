package com.example.miniproject_02;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.miniproject_02.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding bindingViews;
    View root;
    SharedPreferences session , colorSession ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingViews = ActivityMainBinding.inflate(getLayoutInflater());
        root = bindingViews.getRoot();
        setContentView(root);
//      create the sharedPreferences
        session = getSharedPreferences("pin_the_quotes" , MODE_PRIVATE);

        colorSession = getSharedPreferences("bg_colors" , MODE_PRIVATE);

        //region fill the spinner
        ArrayList<String> color_names = new ArrayList<>();
        color_names.add("Default");
        color_names.add("LightSalmon");
        color_names.add("Plum");
        color_names.add("PaleGreen");
        color_names.add("CornflowerBlue");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this ,
                android.R.layout.simple_spinner_dropdown_item , color_names);

        bindingViews.bgColorsSpinner.setAdapter(spinnerAdapter);
        //endregion

        int id = colorSession.getInt("item_id" , 0);

        bindingViews.bgColorsSpinner.setSelection(id);
        bindingViews.bgColorsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SharedPreferences.Editor colorEdit = colorSession.edit();
                String bgColor = null;
                switch (position) {
                    case 0 :
                        bgColor = "#FFFFFF";
                        colorEdit.putInt("item_id" , position);
                        break;
                    case 1 :
                        bgColor = "#FFA07A";
                        colorEdit.putInt("item_id" , position);
                        break;

                    case 2 :
                        bgColor = "#DDA0DD";
                        colorEdit.putInt("item_id" , position);
                        break;
                    case 3 :
                        bgColor = "#98FB98";
                        colorEdit.putInt("item_id" , position);
                        break;
                    case 4 :
                        bgColor = "#6495ED";
                        colorEdit.putInt("item_id" , position);
                        break;
                }
                root.setBackgroundColor(Color.parseColor(bgColor));
                colorEdit.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //region sharedPreferences for pining the quotes
        String quote = session.getString("quote" , null);

        if (quote == null) {
            sendRequestToGetQuotes();
        } else {
            String author = session.getString("author" , null);

            bindingViews.quoteTv.setText(quote);
            bindingViews.authorTv.setText(author);

            bindingViews.pinToggleBtn.setChecked(true);
        }
        //endregion

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