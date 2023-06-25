package com.example.miniproject_02.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.miniproject_02.Models.Quote;

import java.util.ArrayList;

public class FavoriteQuotesSQLiteDB extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Quotes.db";
    public static final String CREATE_FAVORITE_QUOTES = String.format("CREATE TABLE %s (" +
            "%s INTEGER PRIMARY KEY," +
            "%s TEXT," +
            "%s TEXT)" , FavoriteQuotesModel.Infos.TABLE_NAME,
            FavoriteQuotesModel.Infos.COLUMN_ID,
            FavoriteQuotesModel.Infos.COLUMN_QUOTE,
            FavoriteQuotesModel.Infos.COLUMN_AUTHOR);

    public static final String DELETE_FAVORITE_QUOTES = String.format("DROP TABLE IF EXISTS %s" ,
            FavoriteQuotesModel.Infos.TABLE_NAME);

    public FavoriteQuotesSQLiteDB(@Nullable Context context) {
        super(context, DATABASE_NAME , null , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FAVORITE_QUOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_FAVORITE_QUOTES);
        onCreate(db);
    }

    public void addQuote(int id , String quote , String author) {
        SQLiteDatabase db = FavoriteQuotesSQLiteDB.this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FavoriteQuotesModel.Infos.COLUMN_ID , id);
        values.put(FavoriteQuotesModel.Infos.COLUMN_QUOTE , quote);
        values.put(FavoriteQuotesModel.Infos.COLUMN_AUTHOR , author);

        db.insert(FavoriteQuotesModel.Infos.TABLE_NAME , null , values);
    }

    public void addQuote(Quote quote) {
        addQuote(quote.getId() , quote.getQuote() , quote.getAuthor());
    }
    public void deleteQuote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String selection = FavoriteQuotesModel.Infos.COLUMN_ID + " LIKE ?";
        String[] selectionArgs = {Integer.toString(id)};

        db.delete(FavoriteQuotesModel.Infos.TABLE_NAME , selection , selectionArgs);
    }

//    just for test if the data is stored in Sqlite
    public void getAll() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projections = {
                FavoriteQuotesModel.Infos.COLUMN_ID,
                FavoriteQuotesModel.Infos.COLUMN_QUOTE,
                FavoriteQuotesModel.Infos.COLUMN_AUTHOR
        };

        Cursor cursor = db.query(
                FavoriteQuotesModel.Infos.TABLE_NAME,
                projections,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteQuotesModel.Infos.COLUMN_ID));
            String quote = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteQuotesModel.Infos.COLUMN_QUOTE));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteQuotesModel.Infos.COLUMN_AUTHOR));
            Log.e("Sql quotes", String.format("Quote : %d , %s , %s .", id, quote, author));
        }

        cursor.close();
    }


    public ArrayList<Quote> getAllQuotes() {
        SQLiteDatabase db = this.getReadableDatabase();

        ArrayList<Quote> quoteslist = new ArrayList<>();

        String[] projections = {
                FavoriteQuotesModel.Infos.COLUMN_ID,
                FavoriteQuotesModel.Infos.COLUMN_QUOTE,
                FavoriteQuotesModel.Infos.COLUMN_AUTHOR
        };

        Cursor cursor = db.query(
                FavoriteQuotesModel.Infos.TABLE_NAME,
                projections,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(FavoriteQuotesModel.Infos.COLUMN_ID));
            String quote = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteQuotesModel.Infos.COLUMN_QUOTE));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(FavoriteQuotesModel.Infos.COLUMN_AUTHOR));

            quoteslist.add(new Quote(id,quote,author));
//            Log.e("Sql quotes", String.format("getAll: %d , %s , %s ." , id , quote , author));
        }

        cursor.close();
        return quoteslist;
    }

    public boolean isQuoteInDB(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String[] projection = {FavoriteQuotesModel.Infos.COLUMN_ID};
        String selection = projection[0] + " LIKE ?";
        String[] selectionArgs = {Integer.toString(id)};

        Cursor cursor = db.query(FavoriteQuotesModel.Infos.TABLE_NAME ,
                projection ,
                selection ,
                selectionArgs ,
                null ,
                null ,
                null);

        boolean value = cursor.moveToNext();
        cursor.close();
        return value;
    }
}
