package com.example.agrisol.Market;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

    Dialog dialog;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private PriceAdapter adapter;
    private ArrayList<Market> marketList;
    private DatabaseReference ref;
    androidx.appcompat.widget.SearchView searchView;
    public static final String path = "Market_Rate";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_update_market_list );

        searchView =findViewById( R.id.show );
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

        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayShowTitleEnabled( false );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setDisplayShowHomeEnabled( true );
        TextView mTitle = toolbar.findViewById( R.id.toolbar_title );
        mTitle.setText( "Update Market Item" );
        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getApplicationContext(), AdminDashboard.class ) );
                finish();
            }
        } );



        recyclerView = (RecyclerView) findViewById( R.id.market_recyclerView );
        linearLayoutManager = new LinearLayoutManager( this );
        recyclerView.setLayoutManager( linearLayoutManager );
        recyclerView.setHasFixedSize( true );
        marketList = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference().child( path );
        //ref.keepSynced(true);

        ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                marketList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Market market = dataSnapshot.getValue( Market.class );
                    market.setName( dataSnapshot.child( "name" ).getValue().toString() );
                    market.setPrice( dataSnapshot.child( "price" ).getValue().toString() );
                    market.setDistrict( dataSnapshot.child( "district" ).getValue().toString() );
                    market.setDate( dataSnapshot.child( "date" ).getValue().toString() );
                    marketList.add( market );
                }
                adapter = new PriceAdapter( UpdateMarketList.this, marketList );
                recyclerView.setAdapter( adapter );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );

    }

}
