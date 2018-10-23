package com.jobseeker.jobseekerusuario;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.sample.fchat.ChatActivity;
import com.app.sample.fchat.data.SettingsAPI;
import com.app.sample.fchat.data.Tools;
import com.app.sample.fchat.widget.CircleTransform;
import com.jobseeker.jobseekerusuario.Model.Empregador;
import com.jobseeker.jobseekerusuario.Model.Trabalhador;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EmpregadorMenuActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private Trabalhador usuario;
    public ImageView image;
    SettingsAPI set;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empregador_menu);
        set = new SettingsAPI(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        prepareActionBar(toolbar);
        usuario = getIntent().getParcelableExtra("usuario");

        ImageView img =  (ImageView) findViewById(R.id.image55);

        Picasso.with(getBaseContext()).load(set.readSetting("mydp")).resize(150, 150).transform(new CircleTransform()).into(img);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Tools.systemBarLolipop(this);
        }
    }

    private void prepareActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_logout: {
                Intent logoutIntent=new Intent(this,OpenActivity.class);
                logoutIntent.putExtra("mode","logout");
                startActivity(logoutIntent);
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void uploadEmprego(View view) {
        Intent intent = new Intent(this, DadosTrabalhoActivity.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }

    public void changeToTrabalhador(View view) {
        Intent intent = new Intent(this, MainMenuActivity.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }

    public void changeMeusEmpregos(View view) {
        ApiClient.getJobClient().getMeusEmpregos(usuario.getIdGmail()).enqueue(new Callback<List<Empregador>>() {
            public void onResponse(Call<List<Empregador>> call, Response<List<Empregador>> response ){
                if (response.isSuccessful() ) {

                    List<Empregador> jobs = response.body();
                    if(jobs.size() == 0){
                        Toast.makeText(getApplicationContext() , "Você não possui ofertas de empregos registrados", Toast.LENGTH_SHORT).show();
                    }else {
                        Intent intent = new Intent(getBaseContext(), ListJobToChangeActivity.class);
                        intent.putExtra("usuario", usuario);
                        intent.putParcelableArrayListExtra("meusJobs", (ArrayList) jobs);
                        startActivity(intent);
                    }
                } else {
                    System.out.println(response.errorBody());
                    Toast.makeText(getApplicationContext() , "Erro encontrado", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Empregador>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    public void goChat(View view) {
        Intent intent = new Intent(getBaseContext(), ChatActivity.class);
        intent.putExtra("usuario", usuario);
        startActivity(intent);
    }

}
