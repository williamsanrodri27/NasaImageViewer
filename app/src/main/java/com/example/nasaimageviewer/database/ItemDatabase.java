package com.example.nasaimageviewer.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.nasaimageviewer.dao.ItemDao;
import com.example.nasaimageviewer.entities.Item;

@Database(entities = {Item.class}, version = 1)
public abstract class ItemDatabase extends RoomDatabase {

    private static ItemDatabase instance;

    public abstract ItemDao itemDao();

    public static synchronized ItemDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ItemDatabase.class, "item_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
