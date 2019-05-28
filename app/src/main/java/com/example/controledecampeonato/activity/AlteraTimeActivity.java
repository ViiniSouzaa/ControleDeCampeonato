package com.example.controledecampeonato.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.controledecampeonato.R;
import com.example.controledecampeonato.modelo.Campeonato;
import com.example.controledecampeonato.modelo.Time;

import java.util.ArrayList;

public class AlteraTimeActivity extends AppCompatActivity {

    Intent intent;
    ArrayList<String> times;
    Button alteraTime;
    EditText editTextNomeTime;
    int quantTimes;
    ArrayList<Campeonato> campeonatos;
    String nomeCampeonato;
    int time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altera_time);
        intent = getIntent();

        alteraTime = findViewById(R.id.buttonAlteraTime);
        editTextNomeTime = findViewById(R.id.editTextNomeTime2);
        time = intent.getIntExtra("time", -1);
        times = intent.getStringArrayListExtra("times");
        nomeCampeonato = intent.getStringExtra("nomeCampeonato");
        quantTimes = intent.getIntExtra("numeroTimes", 0);
    }

    public void alteraTime(View view) {
        if (validaTime()) {
            String temp = times.get(time);
            temp = editTextNomeTime.getText().toString();
            times.remove(time);
            times.add(temp);
            Intent intent = new Intent(this, NovoCampeonatoActivity.class);
            intent.putStringArrayListExtra("times", times);
            intent.putExtra("numeroTimes", quantTimes);
            intent.putExtra("nomeCampeonato", nomeCampeonato);
            startActivity(intent);
        }else{
            Toast.makeText(this, R.string.insira_nome_time, Toast.LENGTH_LONG).show();
        }
    }

    public boolean validaTime(){
        if(editTextNomeTime.getText().toString().equals(""))
            return false;
        return true;
    }
}