package com.app.sample.fchat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.app.sample.fchat.ActivityChatDetails;
import com.app.sample.fchat.ActivityMain;
import com.app.sample.fchat.ChatActivity;
import com.app.sample.fchat.InitiateChatActivity;
import com.app.sample.fchat.adapter.ChatsListAdapter;
import com.app.sample.fchat.data.ParseFirebaseData;
import com.app.sample.fchat.data.SettingsAPI;
import com.app.sample.fchat.model.ChatMessage;
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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatsFragment2 extends Fragment {

    public RecyclerView recyclerView;

    private LinearLayoutManager mLayoutManager;
    public ChatsListAdapter mAdapter;
    private ProgressBar progressBar;

    public static final String MESSAGE_CHILD = "messages";

    View view;

    ParseFirebaseData pfbd;
    SettingsAPI set;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chat, container, false);
        pfbd = new ParseFirebaseData(getContext());
        set = new SettingsAPI(getContext());

        // activate fragment menu
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(MESSAGE_CHILD);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String totalData = "";
                if (dataSnapshot.getValue() != null)
                    totalData = dataSnapshot.getValue().toString();
                // TODO: 25-05-2017 if number of items is 0 then show something else
                mAdapter = new ChatsListAdapter(getContext(), pfbd.getLastMessageList(totalData));
                recyclerView.setAdapter(mAdapter);

                mAdapter.setOnItemClickListener(new ChatsListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, ChatMessage obj, int position) {
                        if (obj.getReceiver().getId().equals(set.readSetting("myid")))
                            ActivityChatDetails.navigate((ChatActivity) getActivity(), v.findViewById(R.id.lyt_parent), obj.getSender());
                        else if (obj.getSender().getId().equals(set.readSetting("myid")))
                            ActivityChatDetails.navigate((ChatActivity) getActivity(), v.findViewById(R.id.lyt_parent), obj.getReceiver());
                    }
                });

                mAdapter.setOnInfoClickListener(new ChatsListAdapter.OnInfoClickListener2() {
                    @Override
                    public void onInfo2Click(View view, ChatMessage obj, int position) {
                        if (obj.getReceiver().getId().equals(set.readSetting("myid")))
                            getInfoFriend(obj.getSender().getId());
                        else if (obj.getSender().getId().equals(set.readSetting("myid")))
                            getInfoFriend(obj.getReceiver().getId());
                    }
                });

                bindView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (getView() != null)
                    Snackbar.make(getView(), "Could not connect", Snackbar.LENGTH_LONG).show();
            }
        });

        return view;
    }

    public void bindView() {
        try {
            mAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        } catch (Exception e) {
        }

    }

    public void getInfoFriend(String id) {
        ApiClient.getJobClient().getPessoaByGmail(id).enqueue(new Callback<List<Trabalhador>>() {
            public void onResponse(Call<List<Trabalhador>> call, Response<List<Trabalhador>> response ){
                if (response.isSuccessful() ) {
                    Log.v("seekerDebug","Entrou no getInfoFriend");
                    List<Trabalhador> jobs = response.body();
                    if(jobs.size() != 0){
                        Log.v("seekerDebug","Entrou no if");
                        Intent intent = new Intent(getContext(), FriendInfoActivity.class);
                        intent.putExtra("friendInfo", jobs.get(0));
                        startActivity( intent);
                    }
                    Log.v("seekerDebug","Passou o if do getInfoFriend");
                } else {
                    System.out.println(response.errorBody());
                    Log.v("seekerDebug","Erro no click info");
                }
            }
            @Override
            public void onFailure(Call<List<Trabalhador>> call, Throwable t) {
                Log.v("seekerDebug","Grande erro no click info");
                t.printStackTrace();
            }
        });
    }
}
