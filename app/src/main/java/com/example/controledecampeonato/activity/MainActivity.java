package com.example.controledecampeonato.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.controledecampeonato.R;
import com.example.controledecampeonato.dao.CampeonatoDatabase;
import com.example.controledecampeonato.modelo.Campeonato;
import com.example.controledecampeonato.modelo.Time;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String ARQUIVO =
            "com.exemple.controledecampeonato.PREFERENCIAS_CORES";
    private static final String COR = "COR";

    private ConstraintLayout layout;
    private int opBack = Color.GREEN;

    ListView listViewCampeonatos;
    Intent intent;

    private List<Campeonato> campeonatos;
    private ActionMode actionMode;
    private int posicaoSelecionado = -1;
    private View viewSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listViewCampeonatos = findViewById(R.id.listViewCampeonatos);
        layout = findViewById(R.id.layoutPrincipal);

        intent = getIntent();

        listViewCampeonatos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posicaoSelecionado = position;
                alterarSelecionado();
            }
        });

        listViewCampeonatos.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewCampeonatos.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (actionMode != null) {
                    return false;
                }
                posicaoSelecionado = position;
                view.setBackgroundColor(Color.LTGRAY);
                viewSelecionada = view;
                listViewCampeonatos.setEnabled(true);
                actionMode = startSupportActionMode(mActionModeCallback);

                return true;
            }
        });
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

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_editar_excluir, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menuItemAlterar:
                    alterarSelecionado();
                    mode.finish();
                    return true;

                case R.id.menuItemDelete:
                    confirmaExcluir();
                    mode.finish();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            if (viewSelecionada != null) {
                viewSelecionada.setBackgroundColor(Color.TRANSPARENT);
            }
            actionMode = null;
            viewSelecionada = null;

            listViewCampeonatos.setEnabled(true);
        }
    };


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
        campeonatos = CampeonatoDatabase.getDatabase(getApplicationContext()).campeonatoDAO().listaTodos();
        ArrayAdapter<Campeonato> adapter = new ArrayAdapter<>(
                this,android.R.layout.simple_list_item_1,
                 campeonatos);
        listViewCampeonatos.setAdapter(adapter);
    }

    private void alterarSelecionado(){
        Intent intent = new Intent(getApplicationContext(), AlteraCampeonato.class);
        intent.putExtra("idCampeonato", campeonatos.get(posicaoSelecionado).getId());
        startActivity(intent);
    }

    public void confirmaExcluir(){
        AlertDialog.Builder confirma = new AlertDialog.Builder(this);
        confirma.setMessage(getString(R.string.deseja_excluir));
        confirma.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    Campeonato campeonato = CampeonatoDatabase.getDatabase(getApplicationContext()).campeonatoDAO().listaPorId(campeonatos.get(posicaoSelecionado).getId());
                    for(Time time : CampeonatoDatabase.getDatabase(getApplicationContext()).timeDAO().listaPorCampeonato(campeonato.getId())){
                        CampeonatoDatabase.getDatabase(getApplicationContext()).timeDAO().deletar(time);
                    }
                    CampeonatoDatabase.getDatabase(getApplicationContext()).campeonatoDAO().deletar(campeonato);
                    populalista();
            }
        });
        confirma.setNegativeButton(R.string.nao, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        confirma.show();
    }

}
