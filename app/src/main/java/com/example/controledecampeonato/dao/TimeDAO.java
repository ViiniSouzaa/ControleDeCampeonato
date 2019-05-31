package com.example.controledecampeonato.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import com.example.controledecampeonato.modelo.Time;

import java.util.List;

@Dao
public interface TimeDAO extends DaoGenerico<Time> {

    @Query("SELECT * FROM time WHERE id = :id")
    Time listaPorId(long id);

    @Query("SELECT * FROM time WHERE id_campeonato = :id ORDER BY nome ASC")
    List<Time> listaPorCampeonato(long id);

    @Query("SELECT * FROM time ORDER BY nome ASC")
    List<Time> listaTodos();
}
