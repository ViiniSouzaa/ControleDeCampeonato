package com.example.controledecampeonato.modelo;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(foreignKeys = @ForeignKey  (entity = Campeonato.class,
                                    parentColumns = "id",
                                    childColumns = "id_campeonato"))
public class Time {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String nome;
    @ColumnInfo(index = true)
    private long id_campeonato;

    public Time(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_campeonato() {
        return id_campeonato;
    }

    public void setId_campeonato(long id_campeonato) {
        this.id_campeonato = id_campeonato;
    }

    @Override
    public String toString() {
        return getNome();
    }
}
