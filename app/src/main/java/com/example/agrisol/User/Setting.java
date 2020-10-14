package com.example.agrisol.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.agrisol.Login.Login;
import com.example.agrisol.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Setting extends AppCompatActivity {
    FirebaseAuth mAuth;
    String currentUserID;
    DatabaseReference UsersRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);


       // currentUserID = mAuth.getCurrentUser().getUid();

        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Setting");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                finish();
            }
        });



    }

    public void UpdateUser(View view)
    {
        TextView add =findViewById(R.id.updateuser);
        startActivity(new Intent(this, UpdateUserProfile.class));
    }
    public void Signout(View view)
    {

        mAuth = FirebaseAuth.getInstance();

        TextView signout=findViewById(R.id.signout);
        mAuth.signOut();
        senduserTologinActivity();
    }


    private void senduserTologinActivity() {

        Intent loginIntent = new Intent(Setting.this, Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}
