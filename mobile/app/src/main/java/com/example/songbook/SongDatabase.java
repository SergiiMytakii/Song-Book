package com.example.songbook;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Song.class, SongFavorite.class}, version = 8, exportSchema = true)
public abstract class SongDatabase extends RoomDatabase {
    public abstract SongDao songDao();
    public abstract SongFavoriteDao songFavoriteDao();

    private static SongDatabase instance;

    public static synchronized SongDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    SongDatabase.class, "song_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}