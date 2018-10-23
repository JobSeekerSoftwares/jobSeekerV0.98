package com.app.sample.fchat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.app.sample.fchat.adapter.FriendsListAdapter;
import com.app.sample.fchat.data.ParseFirebaseData;
import com.app.sample.fchat.data.ParseFirebaseData2;
import com.app.sample.fchat.data.Tools;
import com.app.sample.fchat.model.Friend;
import com.app.sample.fchat.widget.DividerItemDecoration;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jobseeker.jobseekerusuario.ApiClient;
import com.jobseeker.jobseekerusuario.FriendInfoActivity;
import com.jobseeker.jobseekerusuario.Model.Trabalhador;
import com.jobseeker.jobseekerusuario.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InitiateChatActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private RecyclerView recyclerView;
    private FriendsListAdapter mAdapter;
    List<Friend> friendList;
    String idGmail;
    public static final String USERS_CHILD = "users";
    ParseFirebaseData2 pfbd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_chat);
		idGmail = getIntent().getStringExtra("idGmailEmpregador");
        initToolbar();
        initComponent();
        friendList=new ArrayList<>();
        pfbd = new ParseFirebaseData2(this);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(USERS_CHILD);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String totalData = dataSnapshot.getValue().toString();

                mAdapter = new FriendsListAdapter(InitiateChatActivity.this, pfbd.getUserList(totalData,idGmail));
                recyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, Friend obj, int position) {
                        ActivityChatDetails.navigate((InitiateChatActivity) InitiateChatActivity.this, findViewById(R.id.lyt_parent), obj);
                    }
                });

                mAdapter.setOnInfoClickListener(new FriendsListAdapter.OnInfoClickListener() {
                    @Override
                    public void onInfoClick(View view, Friend obj, int position) {
                        String id = obj.getId();
                        Log.v("seekerDebug","Entrou no click info");
                        getInfoFriend(id);
                    }
                });

                bindView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                    Snackbar.make(getWindow().getDecorView(), "Could not connect", Snackbar.LENGTH_LONG).show();
            }
        });

        // for system bar in lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Tools.systemBarLolipop(this);
        }
    }

    public void getInfoFriend(String id) {
        ApiClient.getJobClient().getPessoaByGmail(id).enqueue(new Callback<List<Trabalhador>>() {
            public void onResponse(Call<List<Trabalhador>> call, Response<List<Trabalhador>> response ){
                if (response.isSuccessful() ) {
                    List<Trabalhador> jobs = response.body();
                    if(jobs.size() != 0){
                        Intent intent = new Intent(InitiateChatActivity.this, FriendInfoActivity.class);
                        intent.putExtra("friendInfo", jobs.get(0));
                        startActivity( intent);
                    }
                } else {
                    System.out.println(response.errorBody());
                    Toast.makeText(getApplicationContext() , "Problemas com o banco de dados, tente novamente mais tarde", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Trabalhador>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getApplicationContext() , "Problemas com o banco de dados, tente novamente mais tarde", Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void initComponent() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
		recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    public void initToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
//        actionBar.setSubtitle(Constant.getFriendsData(this).size()+" friends");
    }

    public void bindView() {
        try {
            mAdapter.notifyDataSetChanged();
        } catch (Exception e) {
        }
    }
}
