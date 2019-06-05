package com.example.controledecampeonato.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.controledecampeonato.R;
import com.example.controledecampeonato.dao.CampeonatoDatabase;
import com.example.controledecampeonato.modelo.Campeonato;
import com.example.controledecampeonato.modelo.Time;

import java.util.List;

public class AlteraCampeonato extends AppCompatActivity {
    Intent intent;
    Button adicionaTime, editaCampeonato, adicionaQtdTimes;
    List<Time> times;
    ListView listViewTimes;
    EditText editTextQtdTimes, editTextNomeCampeonato;
    int qtdeTimes;
    long idCampeonato;
    Campeonato campeonato;

    private ActionMode actionMode;
    private int posicaoSelecionado = -1;
    private View viewSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_campeonato);
        intent = getIntent();

        adicionaTime = findViewById(R.id.buttonAdicionaTime2);
        editaCampeonato = findViewById(R.id.buttonEditaCampeonato);
        adicionaQtdTimes = findViewById(R.id.buttonAdicionaQtdTimes2);
        listViewTimes =  findViewById(R.id.listViewTimes2);
        editTextQtdTimes = findViewById(R.id.editTextQtdTimes2);
        editTextNomeCampeonato = findViewById(R.id.editTextNomeCampeonato2);

        clickLista();
        recuperaAnterior();
        validaQntdTimes();
        populalista();
    }

    public void adicionaNovoTime(View view){
        Intent intent = new Intent(getApplicationContext(), InsereTimeBanco.class);
        intent.putExtra("idCampeonato", idCampeonato);
        startActivity(intent);
    }

    public void alterarCampeonato(View view){
        if(validaNomeCampeonato()) {
            campeonato.setNome(editTextNomeCampeonato.getText().toString());
            CampeonatoDatabase.getDatabase(getApplicationContext()).campeonatoDAO().alterar(campeonato);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }else{
            Toast.makeText(this, R.string.insira_nome_campeonato, Toast.LENGTH_LONG).show();
        }
    }


    public void quantidadeTimes(View view){
        if(!editTextQtdTimes.getText().toString().equals("")){
            qtdeTimes = Integer.parseInt(editTextQtdTimes.getText().toString());
            campeonato.setQntdTimes(qtdeTimes);
            CampeonatoDatabase.getDatabase(getApplicationContext()).campeonatoDAO().alterar(campeonato);
            validaQntdTimes();
            if(qtdeTimes > 0 && qtdeTimes <= 20){
                adicionaTime.setVisibility(View.VISIBLE);
                listViewTimes.setVisibility(View.VISIBLE);
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(editTextQtdTimes.getWindowToken(), 0);
            }else{
                Toast.makeText(this, R.string.insira_quantidade_valida, Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(this, R.string.insira_quantidade_valida, Toast.LENGTH_LONG).show();
        }
    }

    private void validaQntdTimes() {
        times = CampeonatoDatabase.getDatabase(getApplicationContext()).timeDAO().listaPorCampeonato(idCampeonato);
        if(times.size() == qtdeTimes){
            editaCampeonato.setVisibility(View.VISIBLE);
            adicionaTime.setClickable(false);
        }else{
            editaCampeonato.setVisibility(View.INVISIBLE);
            adicionaTime.setClickable(true);
        }
        populalista();
    }

    public void recuperaAnterior(){
        idCampeonato = intent.getLongExtra("idCampeonato", -1);
        campeonato = CampeonatoDatabase.getDatabase(getApplicationContext()).campeonatoDAO().listaPorId(idCampeonato);
        editTextNomeCampeonato.setText(campeonato.getNome());
        qtdeTimes = CampeonatoDatabase.getDatabase(getApplicationContext()).campeonatoDAO().listaPorId(idCampeonato).getQntdTimes();
        times = CampeonatoDatabase.getDatabase(getApplicationContext()).timeDAO().listaPorCampeonato(idCampeonato);
        if(qtdeTimes > 0 && qtdeTimes <= 20){
            editTextQtdTimes.setText(String.valueOf(qtdeTimes));
            validaQntdTimes();
        }
    }

    public void populalista(){
        times = CampeonatoDatabase.getDatabase(getApplicationContext()).timeDAO().listaPorCampeonato(idCampeonato);
        ArrayAdapter<Time> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, times);
        listViewTimes.setAdapter(adapter);
    }

    public boolean validaNomeCampeonato(){
        if (editTextNomeCampeonato.getText().toString().equals(""))
            return false;
        return true;
    }

    private void clickLista() {
       /* listViewTimes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, final int position, long id) {
                final Time time = (Time) adapter.getAdapter().getItem(position);

                final CharSequence[] itens = {getString(R.string.alterar), getString(R.string.excluir), getString(R.string.cancelar)};

                clickItem(itens, time, position);
            }
        });*/

        listViewTimes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                posicaoSelecionado = position;
                alterarSelecionado();
            }
        });

        listViewTimes.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        listViewTimes.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (actionMode != null) {
                    return false;
                }
                posicaoSelecionado = position;
                view.setBackgroundColor(Color.LTGRAY);
                viewSelecionada = view;
                listViewTimes.setEnabled(true);
                actionMode = startSupportActionMode(mActionModeCallback);

                return true;
            }
        });
    }

   /* private void clickItem(final CharSequence[] itens, final Time time, final int position) {
        AlertDialog.Builder opcoes = new AlertDialog.Builder(this);
        opcoes.setItems(itens, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int op) {
                String opcao = (String) itens[op];
                if(opcao.equals(getString(R.string.alterar))){
                    Intent intent = new Intent(getApplicationContext(), AlteraTimeBancoActivity.class);
                    intent.putExtra("time", CampeonatoDatabase.getDatabase(getApplicationContext()).timeDAO().listaPorId(time.getId()).getId());
                    intent.putExtra("idCampeonato", idCampeonato);
                    startActivity(intent);
                    finish();
                }else if(opcao.equals(getString(R.string.excluir))){
                    confirmaExcluir(time);
                }else if(opcao.equals(getString(R.string.cancelar))){
                    dialog.cancel();
                }
            }
        });
        opcoes.show();
    }*/

    public void confirmaExcluir(){
        AlertDialog.Builder confirma = new AlertDialog.Builder(this);
        confirma.setMessage(getString(R.string.deseja_excluir));
        confirma.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CampeonatoDatabase.getDatabase(getApplicationContext()).timeDAO().deletar(times.get(posicaoSelecionado));
                validaQntdTimes();
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

            listViewTimes.setEnabled(true);
        }
    };

    private void alterarSelecionado() {
        Intent intent = new Intent(getApplicationContext(), AlteraTimeBancoActivity.class);
        intent.putExtra("time", times.get(posicaoSelecionado).getId());
        intent.putExtra("idCampeonato", idCampeonato);
        startActivity(intent);
        finish();
    }
}
