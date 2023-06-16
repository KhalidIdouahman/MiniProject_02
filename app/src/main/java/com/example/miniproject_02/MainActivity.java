package com.example.miniproject_02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.miniproject_02.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding bindingViews;
    View root;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindingViews = ActivityMainBinding.inflate(getLayoutInflater());
        root = bindingViews.getRoot();
        setContentView(root);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = getRandomUrls(25 , 80);

        JsonObjectRequest JsonObjReq = new JsonObjectRequest(url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    bindingViews.quoteTv.setText(response.getString("quote"));
                    bindingViews.author.setText(response.getString("author"));
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