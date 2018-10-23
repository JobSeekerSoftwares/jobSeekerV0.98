package com.jobseeker.jobseekerusuario;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.app.sample.fchat.InitiateChatActivity;
import com.app.sample.fchat.data.SettingsAPI;
import com.jobseeker.jobseekerusuario.Model.Empregador;

import java.util.ArrayList;
import java.util.List;

public class MoreInfoActivity extends ListActivity {
    private ArrayAdapter<String> adapter;
    private ArrayList<String> wordList;
    private Empregador job;
    SettingsAPI set;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        wordList = new ArrayList<String>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);
        job = getIntent().getParcelableExtra("job");
        if(job == null){
            Toast.makeText(getApplicationContext() , "Erro encontrado, nao clique nas opcoes", Toast.LENGTH_SHORT).show();
            wordList.add("Erro");
        }else{
            wordList.add("Nome: "+job.getNome());
            wordList.add("Email: "+job.getEmail());
            wordList.add("Requisitos: "+job.getRequisitos());
            wordList.add("Experiência necessária: "+job.getExperiencia());
            wordList.add("Formação desejada: "+job.getFormacao());
            wordList.add("Período: "+job.getDisponibilidade());
            wordList.add("Local: "+job.getLocal());
            wordList.add("Salário: "+job.getSalario());

        }

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1,
                wordList);

        setListAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void goInitiateChat(View view) {
        set = new SettingsAPI(this);
        if (set.readSetting("myid") == job.getIdGmail()) {
            Toast.makeText(getApplicationContext(), "Você é quem ofertou este emprego", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(getBaseContext(), InitiateChatActivity.class);
            intent.putExtra("idGmailEmpregador", job.getIdGmail());
            startActivity(intent);
        }
    }

}
