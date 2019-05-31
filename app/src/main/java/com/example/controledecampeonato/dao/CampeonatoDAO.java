package com.example.controledecampeonato.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.example.controledecampeonato.modelo.Campeonato;

import java.util.List;

@Dao
public interface CampeonatoDAO extends DaoGenerico<Campeonato> {

    @Query("SELECT * FROM campeonato WHERE id = :id")
    Campeonato listaPorId(long id);

    @Query("SELECT * FROM campeonato ORDER BY nome ASC")
    List<Campeonato> listaTodos();

    @Query("SELECT * FROM campeonato WHERE id = (SELECT MAX(id) from campeonato)")
    Campeonato listaUltimoInserido();
}
