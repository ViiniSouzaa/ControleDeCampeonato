package com.example.controledecampeonato.dao;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

public abstract class TimeDatabase extends RoomDatabase {

    public abstract TimeDAO timeDAO();

    private static TimeDatabase instance;

    public static TimeDatabase getDatabase(final Context context) {

        if (instance == null) {

            synchronized (TimeDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context,
                            TimeDatabase.class,
                            "time.db").allowMainThreadQueries().build();
                }
            }
        }
        return instance;
    }
}
