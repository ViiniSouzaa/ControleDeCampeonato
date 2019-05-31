package com.example.controledecampeonato.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

@Dao
public interface DaoGenerico<T> {
    @Insert
    long inserir(T entidade);

    @Delete
    void deletar(T entidade);

    @Update
    void alterar(T entidade);
}
