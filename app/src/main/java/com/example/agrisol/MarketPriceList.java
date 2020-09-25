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

public class MarketPriceList extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private MarketAdapter marketAdapter;
    private DatabaseReference databaseReference;
    private ArrayList<Market> marketList;
    private Context mcontext;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.market_price_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        marketList = new ArrayList<Market>();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView_price);
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


        GetData();

    }

    private void GetData(){

        Query query =databaseReference.child("Market Rate");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Market experts =new Market();
                    experts.setCropName(snapshot.child("CropName").getValue().toString());
                    experts.setCropPrice(snapshot.child("CropPrice").getValue().toString());
                    experts.setDistrict(snapshot.child("CropDistrict").getValue().toString());

                    marketList.add(experts);

                }
                marketAdapter =new MarketAdapter( mcontext,marketList);
                recyclerView.setAdapter(marketAdapter);
                marketAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

}
