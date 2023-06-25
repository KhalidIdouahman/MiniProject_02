package com.example.miniproject_02.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.miniproject_02.Models.ColorModel;
import com.example.miniproject_02.Models.Settings;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsSQLiteDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Settings.db";
    public static final int DATABASE_VERSION = 1;

    public static final String SQL_CREATION_COLOR_QUERY  = String.format("CREATE TABLE %s ( " +
            "%s TEXT PRIMARY KEY, " +
            "%s TEXT)" , SettingsModelDB.Color.TABLE_NAME ,SettingsModelDB.Color.COLUMN_NAME , SettingsModelDB.Color.COLUMN_CODE);

    public static final String SQL_DELETION_COLOR_QUERY = String.format("DROP TABLE IF EXISTS %s",
            SettingsModelDB.Color.TABLE_NAME);

    public static final String SQL_CREATION_SETTINGS_QUERY = String.format("CREATE TABLE %s ( " +
                    "%s TEXT PRIMARY KEY, " +
                    "%s TEXT REFERENCES %s(%s))",
            SettingsModelDB.Settings.TABLE_NAME,
            SettingsModelDB.Settings.COLUMN_NAME,
            SettingsModelDB.Settings.COLUMN_VALUE,
            SettingsModelDB.Color.TABLE_NAME,
            SettingsModelDB.Color.COLUMN_NAME);

    public static final String SQL_DELETION_SETTINGS_QUERY = String.format("DROP TABLE IF EXISTS %s",
            SettingsModelDB.Settings.TABLE_NAME);


    public SettingsSQLiteDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATION_COLOR_QUERY);
        db.execSQL(SQL_CREATION_SETTINGS_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETION_COLOR_QUERY);
        db.execSQL(SQL_DELETION_SETTINGS_QUERY);
        onCreate(db);
    }

    public void addColor(ColorModel colorModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SettingsModelDB.Color.COLUMN_NAME , colorModel.getName());
        values.put(SettingsModelDB.Color.COLUMN_CODE , colorModel.getCode());
        db.insert(SettingsModelDB.Color.TABLE_NAME , null , values);
    }

    public ArrayList<ColorModel> getColors() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {SettingsModelDB.Color.COLUMN_NAME ,
                                SettingsModelDB.Color.COLUMN_CODE};

        Cursor cursor = db.query(SettingsModelDB.Color.TABLE_NAME , projection , null , null
                , null , null , null);

        ArrayList<ColorModel> data = new ArrayList<>();
        while (cursor.moveToNext()) {
            data.add(new ColorModel(cursor.getString(cursor.getColumnIndexOrThrow(projection[0])),
                    cursor.getString(cursor.getColumnIndexOrThrow(projection[1]))));
        }
        cursor.close();
        return data;
    }

    public void addBgColor() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SettingsModelDB.Settings.COLUMN_NAME , "bg_color");
        values.put(SettingsModelDB.Settings.COLUMN_VALUE , "Default");
        db.insert(SettingsModelDB.Settings.TABLE_NAME , null , values);
    }

    public void updateBgColor(Settings bgColor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SettingsModelDB.Settings.COLUMN_VALUE , bgColor.getValue());

//        // Define the WHERE clause for the update
        String whereClause = SettingsModelDB.Settings.COLUMN_NAME + " = ?";
        String[] whereArgs = { bgColor.getName() };
        int rowAffected = db.update(SettingsModelDB.Settings.TABLE_NAME, values, whereClause, whereArgs);
    }
    public String getBgColor() {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {SettingsModelDB.Settings.COLUMN_VALUE};
        Cursor cursor = db.query(SettingsModelDB.Settings.TABLE_NAME , projection , null , null , null ,null , null);
        String colorName = null;
        while (cursor.moveToNext()) {
            colorName = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
        }
        return colorName;
    }
}
