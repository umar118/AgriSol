package com.example.agrisol.Market;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrisol.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MarketPriceList extends Fragment  {

    RecyclerView recyclerView;
    ArrayList<Market> marketList;
    MarketAdapter adapter;
    DatabaseReference ref;
    SearchView searchView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.market_price_list, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView =getView().findViewById(R.id.recyclerView_price);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        marketList =new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference().child("Market Rate");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        Market market =dataSnapshot.getValue(Market.class);
                        market.setName(dataSnapshot.child("CropName").getValue().toString());
                        market.setPrice(dataSnapshot.child("CropPrice").getValue().toString());
                        market.setDistrict(dataSnapshot.child("CropDistrict").getValue().toString());
                        market.setDate(dataSnapshot.child("Current_Date").getValue().toString());
                        marketList.add(market);
                    }
                    adapter = new MarketAdapter(MarketPriceList.this.getContext(),marketList);
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        searchView = (SearchView) getView().findViewById( R.id.search );
        searchView.setOnQueryTextListener( new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter( newText );
                return false;
            }
        } );

    }




}
