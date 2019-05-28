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

public class CadastrarTimeActivity  extends AppCompatActivity {

    Intent intent;
    ArrayList<String> times;
    Button adicionarNovoTime;
    EditText editTextNomeTime;
    int quantTimes;
    ArrayList<Campeonato> campeonatos;
    String nomeCampeonato;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_time);
        intent = getIntent();

        adicionarNovoTime = findViewById(R.id.buttonAdicionaNovoTime);
        editTextNomeTime = findViewById(R.id.editTextNomeTime);
        times = intent.getStringArrayListExtra("times");
        nomeCampeonato = intent.getStringExtra("nomeCampeonato");
        quantTimes = intent.getIntExtra("numeroTimes", 0);
    }

    public void adicionaNovoTime(View view) {
        if (validaTime()) {
            String time = editTextNomeTime.getText().toString();
            times.add(time);
            Intent intent = new Intent(this, NovoCampeonatoActivity.class);
            intent.putStringArrayListExtra("times", times);
            intent.putExtra("numeroTimes", quantTimes);
            intent.putExtra("nomeCampeonato", nomeCampeonato);
            startActivity(intent);
            this.finish();
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
