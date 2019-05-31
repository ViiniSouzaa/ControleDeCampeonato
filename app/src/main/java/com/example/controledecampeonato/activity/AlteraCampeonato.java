package com.example.controledecampeonato.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

        populalista();
        recuperaAnterior();
        clickLista();
        validaQntdTimes();
    }

    public void adicionaNovoTime(View view){
        Intent intent = new Intent(this, CadastrarTimeActivity.class);
        intent.putExtra("nomeCampeonato", editTextNomeCampeonato.getText().toString());
        intent.putExtra("numeroTimes", qtdeTimes);
        startActivity(intent);
    }

    public void adicionaNovoCampeonato(View view){
        if(validaNomeCampeonato()) {
            String nome = editTextNomeCampeonato.getText().toString();
            Campeonato campeonato = new Campeonato(nome);
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
        for (Time time : times){
            time.setId_campeonato(id);
            CampeonatoDatabase.getDatabase(getApplicationContext()).timeDAO().inserir(time);
        }
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
        System.out.println(idCampeonato);
        qtdeTimes = CampeonatoDatabase.getDatabase(getApplicationContext()).campeonatoDAO().listaPorId(idCampeonato).getQntdTimes();
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
        listViewTimes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, final int position, long id) {
                final Time time = (Time) adapter.getAdapter().getItem(position);

                final CharSequence[] itens = {getString(R.string.alterar), getString(R.string.excluir), getString(R.string.cancelar)};

                AlertDialog.Builder opcoes = new AlertDialog.Builder(getApplicationContext());
                opcoes.setItems(itens, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int op) {
                        String opcao = (String) itens[op];
                        if(opcao.equals(getString(R.string.alterar))){
                            Intent intent = new Intent(getApplicationContext(), AlteraTimeBancoActivity.class);
                            intent.putExtra("time", CampeonatoDatabase.getDatabase(getApplicationContext()).timeDAO().listaPorId(position).getId());
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
            }
        });
    }

    public void confirmaExcluir(final Time time){
        AlertDialog.Builder confirma = new AlertDialog.Builder(this);
        confirma.setMessage(getString(R.string.deseja_excluir));
        confirma.setPositiveButton(R.string.sim, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CampeonatoDatabase.getDatabase(getApplicationContext()).timeDAO().deletar(time);
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
