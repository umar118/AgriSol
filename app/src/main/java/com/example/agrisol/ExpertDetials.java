package com.example.agrisol;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExpertDetials extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private ExpertAdapter expertAdapter;
    private DatabaseReference databaseReference;
    private ArrayList<Experts> expertsList;
    private Context context;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.expert_detials, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        expertsList = new ArrayList<Experts>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
      //  addTaskBox = (EditText) findViewById(R.id.add_task_box);

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView1);
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        GetData();

    }

    private void GetData(){

        Query query =databaseReference.child("Users").child("Expert");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Experts experts =new Experts();
                    experts.setProfile(snapshot.child("expertprofileimage").getValue().toString());
                    experts.setExpert_Fullname(snapshot.child("expert_FullName").getValue().toString());
                    experts.setExpert_Email(snapshot.child("expert_Email").getValue().toString());
                    experts.setExpert_Contact(snapshot.child("expert_MobileNo").getValue().toString());
                    experts.setExpert_Qualification(snapshot.child("expert_Qualification").getValue().toString());
                    experts.setExpert_City(snapshot.child("expert_City").getValue().toString());
                    experts.setExpert_Province(snapshot.child("expert_Province").getValue().toString());
                    experts.setExpert_Country(snapshot.child("expert_Country").getValue().toString());
                    experts.setExpert_Experties(snapshot.child("expert_Experties").getValue().toString());

                    expertsList.add(experts);

                }
                expertAdapter =new ExpertAdapter(context,expertsList);
                recyclerView.setAdapter(expertAdapter);
                expertAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}


