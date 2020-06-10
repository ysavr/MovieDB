package com.savr.moviedb.local;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.savr.moviedb.model.room.User;

@Database(entities = User.class, version = AppDatabase.DATABASE_VERSION)
public abstract class AppDatabase extends RoomDatabase{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Movie-Database-Room";

    public abstract UserDAO userDAO();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }

}
