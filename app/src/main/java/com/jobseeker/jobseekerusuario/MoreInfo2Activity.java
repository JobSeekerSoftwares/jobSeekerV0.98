package com.jobseeker.jobseekerusuario;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.app.sample.fchat.InitiateChatActivity;
import com.app.sample.fchat.data.SettingsAPI;
import com.jobseeker.jobseekerusuario.Model.Empregador;

import java.util.ArrayList;

public class MoreInfo2Activity extends AppCompatActivity {
    private ArrayAdapter<String> adapter;
    private ArrayList<String> wordList;
    private Empregador job;
    EditText cargo, requisitos, formacao, experiencia, salario, local, disponibilidade, email;
    SettingsAPI set;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        wordList = new ArrayList<String>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info2);
        job = getIntent().getParcelableExtra("job");
        if(job == null){
            Toast.makeText(getApplicationContext() , "Erro encontrado, nao clique nas opcoes", Toast.LENGTH_SHORT).show();
            wordList.add("Erro");
        }else{
            cargo = (EditText) findViewById(R.id.editText1);
            email = (EditText) findViewById(R.id.editText2);
            requisitos = (EditText) findViewById(R.id.editText3);
            formacao= (EditText) findViewById(R.id.editText5);
            experiencia = (EditText) findViewById(R.id.editText4);
            salario = (EditText) findViewById(R.id.editText8);
            local = (EditText) findViewById(R.id.editText7);
            disponibilidade = (EditText) findViewById(R.id.editText6);

            cargo.setText(job.getNome());
            email.setText(job.getEmail());
            requisitos.setText(job.getRequisitos());
            experiencia.setText(job.getExperiencia());
            formacao.setText(job.getFormacao());
            disponibilidade.setText(job.getDisponibilidade());
            local.setText(job.getLocal());
            salario.setText(job.getSalario());

        }





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
