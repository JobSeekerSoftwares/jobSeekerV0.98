package com.jobseeker.jobseekerusuario;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.app.sample.fchat.InitiateChatActivity;
import com.jobseeker.jobseekerusuario.Model.Empregador;
import com.jobseeker.jobseekerusuario.Model.Trabalhador;

import java.util.ArrayList;

public class FriendInfoActivity extends ListActivity {
    private ArrayAdapter<String> adapter;
    private ArrayList<String> wordList;
    private Trabalhador job;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        wordList = new ArrayList<String>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);
        job = getIntent().getParcelableExtra("friendInfo");
        if(job == null){
            Toast.makeText(getApplicationContext() , "Erro encontrado, nao clique nas opcoes", Toast.LENGTH_SHORT).show();
            wordList.add("Erro");
        }else{
            wordList.add("Nome: "+job.getNome());
            wordList.add("Email: "+job.getEmail());
            wordList.add("Objetivo: "+job.getObjetivo());
            wordList.add("Perfil: "+job.getPerfil());
            wordList.add("Experiência: "+job.getExperiencia());
            wordList.add("Formação: "+job.getFormacao());
            wordList.add("Cursos: "+job.getCursoComplementar());
            wordList.add("Idiomas: "+job.getIdiomas());
            wordList.add("Dados complementares: "+job.getInfoProfissional());

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

}
