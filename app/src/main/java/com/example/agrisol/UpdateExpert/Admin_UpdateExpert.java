package com.example.agrisol.UpdateExpert;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrisol.MainActivity;
import com.example.agrisol.R;
import com.example.agrisol.UpdateExpertAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Admin_UpdateExpert extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private UpdateExpertAdapter expertAdapter;
    private DatabaseReference databaseReference;
    private ArrayList<UpdateExpert> expertsList;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin__update_expert);

        expertsList = new ArrayList<UpdateExpert>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        //  addTaskBox = (EditText) findViewById(R.id.add_task_box);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_expert);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        GetData() ;
    }

    private void GetData(){

        Query query =databaseReference.child("Users").child("Expert");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    UpdateExpert experts =new UpdateExpert();
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
                expertAdapter =new UpdateExpertAdapter(context,expertsList);
                recyclerView.setAdapter(expertAdapter);
                expertAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
