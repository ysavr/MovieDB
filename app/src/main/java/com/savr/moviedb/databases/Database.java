package com.savr.moviedb.databases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class Database extends SQLiteAssetHelper {
    private static final String DB_Name = "MovieDB.db";
    private static final int DB_VER = 1;

    public Database(Context context) {
        super(context, DB_Name,null, DB_VER);
    }

    public void addFavorites(String movieId){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT INTO Favorites(movieId) VALUES ('%s');",movieId);
        db.execSQL(query);
    }

    public void removeFavorites(String movieId){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE FROM Favorites WHERE movieId='%s';",movieId);
        db.execSQL(query);
    }

    public boolean isFavorites(String movieId){
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT * FROM Favorites WHERE movieId='%s';",movieId);
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}
