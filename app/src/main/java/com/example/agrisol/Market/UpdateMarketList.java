package com.example.agrisol.Market;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrisol.Admin.AdminDashboard;
import com.example.agrisol.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UpdateMarketList extends AppCompatActivity {


    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
   private PriceAdapter adapter;
    private ArrayList<Market> marketList;
    private DatabaseReference ref;
    public   static final String path ="Market Rate";
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_market_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.market_recyclerView);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        marketList = new ArrayList<>();

        ref =FirebaseDatabase.getInstance().getReference().child(path);
        //ref.keepSynced(true);

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
                adapter = new PriceAdapter(UpdateMarketList.this,marketList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

}




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_item,menu);
        MenuItem item =menu.findItem(R.id.app_bar_search);
        SearchView searchView= (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               adapter.getFilter().filter( newText );
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }



}
