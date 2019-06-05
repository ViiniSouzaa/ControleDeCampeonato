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

import java.util.ArrayList;

public class NovoCampeonatoActivity extends AppCompatActivity {

    Intent intent;
    Button adicionaTime, adicionaCampeonato, adicionaQtdTimes;
    ArrayList<String> times;
    ListView listViewTimes;
    EditText editTextQtdTimes, editTextNomeCampeonato;
    int qtdeTimes;
    ArrayList<Campeonato> campeonatos;

    private ActionMode actionMode;
    private int posicaoSelecionado = -1;
    private View viewSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_novo_campeonato);
        intent = getIntent();

        adicionaTime = findViewById(R.id.buttonAdicionaTime);
        adicionaCampeonato = findViewById(R.id.buttonAdicionaCampeonato);
        adicionaQtdTimes = findViewById(R.id.buttonAdicionaQtdTimes);
        listViewTimes =  findViewById(R.id.listViewTimes);
        editTextQtdTimes = findViewById(R.id.editTextQtdTimes);
        editTextNomeCampeonato = findViewById(R.id.editTextNomeCampeonato);

        recuperaTimes();
        qtdeTimes = intent.getIntExtra("numeroTimes", 0);
        editTextNomeCampeonato.setText(intent.getStringExtra("nomeCampeonato"));
        recuperaAnterior();
        populalista();
        clickLista();

    }

    private void recuperaTimes() {
        if(intent.getStringArrayListExtra("times") == null){
            times = new ArrayList<>();
        }else{
            times = intent.getStringArrayListExtra("times");
        }
    }


    public void adicionaNovoTime(View view){
        Intent intent = new Intent(this, CadastrarTimeActivity.class);
        intent.putStringArrayListExtra("times", times);
        intent.putExtra("nomeCampeonato", editTextNomeCampeonato.getText().toString());
        intent.putExtra("numeroTimes", qtdeTimes);
        startActivity(intent);
    }

    public void adicionaNovoCampeonato(View view){
        if(validaNomeCampeonato()) {
            String nome = editTextNomeCampeonato.getText().toString();
            Campeonato campeonato = new Campeonato(nome);
            campeonato.setQntdTimes(qtdeTimes);
            CampeonatoDatabase.getDatabase(getApplicationContext()).campeonatoDAO().inserir(campeonato);
            insereTimesCampeonato(CampeonatoDatabase.getDatabase(getApplicationContext()).campeonatoDAO().listaUltimoInserido().getId());
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }else{
            Toast.makeText(this, R.string.insira_nome_campeonato, Toast.LENGTH_LONG).show();
        }
    }

    private void insereTimesCampeonato(long id) {
        ArrayList<Time> times = transformaTime();
        for (Time time : times){
            time.setId_campeonato(id);
            CampeonatoDatabase.getDatabase(getApplicationContext()).timeDAO().inserir(time);
        }
    }

    public ArrayList<Time> transformaTime(){
        ArrayList<Time> timesTransformados =  new ArrayList<>();
        for(String time : times){
            timesTransformados.add(new Time(time));
        }
        return timesTransformados;
    }

    public void quantidadeTimes(View view){
        if(!editTextQtdTimes.getText().toString().equals("")){
            qtdeTimes = Integer.parseInt(editTextQtdTimes.getText().toString());
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
        if(times.size() == qtdeTimes){
            adicionaCampeonato.setVisibility(View.VISIBLE);
            adicionaTime.setClickable(false);
        }else{
            adicionaCampeonato.setVisibility(View.INVISIBLE);
            adicionaTime.setClickable(true);
        }
    }

    public void recuperaAnterior(){
        if(qtdeTimes > 0 && qtdeTimes <= 20){
            adicionaTime.setVisibility(View.VISIBLE);
            listViewTimes.setVisibility(View.VISIBLE);
            editTextQtdTimes.setText(String.valueOf(qtdeTimes));
            validaQntdTimes();
        }
    }
    public void populalista(){
        ArrayAdapter<Time> adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, transformaTime());
        listViewTimes.setAdapter(adapter);
    }

    public boolean validaNomeCampeonato(){
        if (editTextNomeCampeonato.getText().toString().equals(""))
            return false;
        return true;
    }

    private void clickLista() {
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

    public void alterarSelecionado(){
        Intent intent = new Intent(NovoCampeonatoActivity.this, AlteraTimeActivity.class);
        intent.putExtra("time", posicaoSelecionado);
        intent.putStringArrayListExtra("times", times);
        intent.putExtra("nomeCampeonato", editTextNomeCampeonato.getText().toString());
        intent.putExtra("numeroTimes", qtdeTimes);
        startActivity(intent);

    }
    public void confirmaExcluir(){
        AlertDialog.Builder confirma = new AlertDialog.Builder(this);
        confirma.setMessage(getString(R.string.deseja_excluir));
        confirma.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                times.remove(times.get(posicaoSelecionado));
                populalista();
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
}