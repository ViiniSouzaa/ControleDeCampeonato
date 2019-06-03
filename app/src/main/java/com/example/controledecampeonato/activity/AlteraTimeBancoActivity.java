package com.example.controledecampeonato.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.controledecampeonato.R;
import com.example.controledecampeonato.dao.CampeonatoDatabase;
import com.example.controledecampeonato.modelo.Time;


public class AlteraTimeBancoActivity extends AppCompatActivity {
    Intent intent;
    EditText editTextNomeTime;
    long idtime;
    Time timeTemp;
    long idCamp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altera_time);
        intent = getIntent();

        editTextNomeTime = findViewById(R.id.editTextNomeTime2);
        idtime = intent.getLongExtra("time", -1);
        idCamp = intent.getLongExtra("idCampeonato", -1);

        timeTemp = CampeonatoDatabase.getDatabase(getApplicationContext()).timeDAO().listaPorId(idtime);

        editTextNomeTime.setText(timeTemp.getNome());
    }

    public void alteraTime(View view) {
        if (validaTime()) {
            timeTemp.setNome(editTextNomeTime.getText().toString());
            CampeonatoDatabase.getDatabase(getApplicationContext()).timeDAO().alterar(timeTemp);
            Intent intent = new Intent(this, AlteraCampeonato.class);
            intent.putExtra("idCampeonato", idCamp);
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
