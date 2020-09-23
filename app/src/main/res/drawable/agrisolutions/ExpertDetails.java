package com.example.agrisolutions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ExpertDetails extends Fragment {

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    ArrayList<ExpertDetailsViewModel> list;
    ExpertAdapter adapter;
    String ids;
    FirebaseUser user;
    String title;
    String taskerProffession;

    private ExpertDetailsViewModel mViewModel;

    public static ExpertDetails newInstance() {
        return new ExpertDetails();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.expert_details_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       // mViewModel = ViewModelProviders.of(this).get(ExpertDetailsViewModel.class);
        // TODO: Use the ViewModel


        recyclerView = getView().findViewById(R.id.recycler_expert);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        list = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mRefrence = FirebaseDatabase.getInstance().getReference("Users").child("Expert");

        mRefrence.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                taskerProffession = String.valueOf(dataSnapshot.child("taskerProfession").getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        DatabaseReference postrefrence;
        postrefrence = FirebaseDatabase.getInstance().getReference("All_Experts");
        postrefrence.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    list.clear();
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        ids = dataSnapshot1.getKey();
                        databaseReference = FirebaseDatabase.getInstance().getReference("All_Experts").child(ids);
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                list.clear();
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    title = String.valueOf(dataSnapshot1.child("title").getValue());
                                    if (taskerProffession.equals(title)) {
                                        ExpertDetailsViewModel p = dataSnapshot1.getValue(ExpertDetailsViewModel.class);
                                        list.add(p);
                                    }
                                }
                            //    adapter = new ExpertAdapter(Tasker_View_Post.this, list);
                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }// else setContentView(R.layout.no_post_yet);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}