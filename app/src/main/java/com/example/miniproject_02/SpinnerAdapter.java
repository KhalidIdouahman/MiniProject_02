package com.example.miniproject_02;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.miniproject_02.Models.ColorModel;

import java.util.ArrayList;

public class SpinnerAdapter implements android.widget.SpinnerAdapter {

    ArrayList<ColorModel> colorsList;
    Context context;

    public SpinnerAdapter(Context context ,ArrayList<ColorModel> colorsList) {
        this.context = context;
        this.colorsList = colorsList;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner ,parent , false);
        }
        ColorModel color = colorsList.get(position);

        TextView colorNameTv = convertView.findViewById(R.id.color_name_tv);
        TextView colorCodeEt = convertView.findViewById(R.id.color_code_et);

        String colorCode = color.getCode();
        colorNameTv.setText(color.getName());
        colorCodeEt.setText(colorCode);
        colorCodeEt.setBackgroundColor(Color.parseColor(colorCode));
        return convertView;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return colorsList.size();
    }

    @Override
    public Object getItem(int position) {
        return colorsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_spinner ,parent , false);
        }
        ColorModel color = colorsList.get(position);

        TextView colorNameTv = convertView.findViewById(R.id.color_name_tv);
        TextView colorCodeEt = convertView.findViewById(R.id.color_code_et);

        String colorCode = color.getCode();
        colorNameTv.setText(color.getName());
        colorCodeEt.setText(colorCode);
        colorCodeEt.setBackgroundColor(Color.parseColor(colorCode));
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
