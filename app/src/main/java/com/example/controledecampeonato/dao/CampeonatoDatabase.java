package com.example.controledecampeonato.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.controledecampeonato.modelo.Campeonato;
import com.example.controledecampeonato.modelo.Time;

@Database(entities = {Campeonato.class, Time.class}, version = 1, exportSchema = false)
public abstract class CampeonatoDatabase  extends RoomDatabase {

    public abstract CampeonatoDAO campeonatoDAO();
    public abstract TimeDAO timeDAO();

    private static CampeonatoDatabase instance;

    public static CampeonatoDatabase getDatabase(final Context context) {

        if (instance == null) {

            synchronized (CampeonatoDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context,
                            CampeonatoDatabase.class,
                            "campeonatos.db").allowMainThreadQueries().build();
                }
            }
        }
        return instance;
    }
}

