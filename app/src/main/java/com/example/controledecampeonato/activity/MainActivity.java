package com.example.controledecampeonato.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.controledecampeonato.R;
import com.example.controledecampeonato.dao.CampeonatoDAO;
import com.example.controledecampeonato.dao.CampeonatoDatabase;
import com.example.controledecampeonato.modelo.Campeonato;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String ARQUIVO =
            "com.exemple.controledecampeonato.PREFERENCIAS_CORES";
    private static final String COR = "COR";

    private ConstraintLayout layout;
    private int opBack = Color.GREEN;

    ListView listViewCampeonatos;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewCampeonatos = findViewById(R.id.listViewCampeonatos);
        layout = findViewById(R.id.layoutPrincipal);

        intent = getIntent();
        populalista();
        lerPreferenciaCor();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item;
        switch (opBack){
            case Color.GREEN :
                item = menu.findItem(R.id.menuItemVerde);
                break;
            case Color.BLACK:
                item = menu.findItem(R.id.menuItemPreto);
                break;
            default:
                return false;
        }
        item.setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);

        switch (item.getItemId()){
            case R.id.menuItemVerde :
                SalvarPreferenciaBack(Color.GREEN);
                return true;
            case R.id.menuItemPreto:
                SalvarPreferenciaBack(Color.BLACK);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void SalvarPreferenciaBack(int color) {
        SharedPreferences shared = getSharedPreferences(ARQUIVO,
                Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = shared.edit();

        editor.putInt(COR, color);

        editor.commit();

        opBack = color;

        mudaCorFundo();
    }

    private void mudaCorFundo() {
        layout.setBackgroundColor(opBack);
    }

    private void lerPreferenciaCor(){

        SharedPreferences shared = getSharedPreferences(ARQUIVO,
                Context.MODE_PRIVATE);

        opBack = shared.getInt(COR, opBack);

        mudaCorFundo();
    }

    public void cadastraCampeonato(View view){
        Intent intent =  new Intent(getBaseContext(), NovoCampeonatoActivity.class);
        startActivity(intent);
    }

    public void sobre(View view){
        startActivity(new Intent(getBaseContext(), SobreActivity.class));
    }

    public void populalista(){
        ArrayAdapter<Campeonato> adapter = new ArrayAdapter<>(
                this,android.R.layout.simple_list_item_1,
                 CampeonatoDatabase.getDatabase(getApplicationContext()).campeonatoDAO().listaTodos());
        listViewCampeonatos.setAdapter(adapter);
    }
}
