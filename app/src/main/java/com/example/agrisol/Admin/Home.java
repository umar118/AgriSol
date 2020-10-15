package com.example.agrisol.Admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.agrisol.R;

public class Home extends AppCompatActivity {
    TextView addMarket,adminHome,shareApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.home );

        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayShowTitleEnabled( false );
        TextView mTitle = toolbar.findViewById( R.id.toolbar_title );
        mTitle.setText( "Admin Home" );

        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setDisplayShowHomeEnabled( true );

        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getApplicationContext(), AdminDashboard.class ) );
                finish();
            }
        } );


        addMarket = (TextView) findViewById( R.id.add_market );
        addMarket.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getApplicationContext(), AddMarketList.class ) );
                finish();
            }
        } );

        adminHome = (TextView) findViewById( R.id.admin_home );
        adminHome.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getApplicationContext(), AdminDashboard.class ) );
                finish();
            }
        } );

        shareApp = (TextView) findViewById( R.id.share );
        shareApp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareIt();
            }
        } );
        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();




    }

    private void shareIt() {

        Intent intent =new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "AgriSolutions");
        intent.putExtra(Intent.EXTRA_TEXT,"Learn Android App Development https://play.google.com/store/apps/details?id=com.tutorial.personal.androidstudiopro ");
        intent.setType("text/plain");
        startActivity(intent);

    }
}
