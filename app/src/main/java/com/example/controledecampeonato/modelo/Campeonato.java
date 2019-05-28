package com.example.controledecampeonato.modelo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;

@Entity
public class Campeonato {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @NonNull
    private String nome;

    public Campeonato(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    @Override
    public String toString() {
        return getNome();
    }
}
