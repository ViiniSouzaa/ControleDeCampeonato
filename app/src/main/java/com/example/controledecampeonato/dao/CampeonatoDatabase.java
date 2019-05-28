package com.example.controledecampeonato.dao;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


public abstract class CampeonatoDatabase  extends RoomDatabase {

    public abstract CampeonatoDAO campeonatoDAO();

    private static CampeonatoDatabase instance;

    public static CampeonatoDatabase getDatabase(final Context context) {

        if (instance == null) {

            synchronized (CampeonatoDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context,
                            CampeonatoDatabase.class,
                            "campeonato.db").allowMainThreadQueries().build();
                }
            }
        }
        return instance;
    }
}

