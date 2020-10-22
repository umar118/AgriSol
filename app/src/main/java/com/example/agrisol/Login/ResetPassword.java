package com.example.agrisol.Login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.agrisol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private EditText Reset;
    private Button emailSend;
    private FirebaseAuth auth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reset_password);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Reset Password");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Reset = findViewById( R.id.reset_password_email );
        emailSend = findViewById( R.id.btnSendEmail);
        auth = FirebaseAuth.getInstance();
       try {


           emailSend.setOnClickListener( new View.OnClickListener() {
               @Override
               public void onClick(View v) {


                   String userEmail = Reset.getText().toString();
                   if (TextUtils.isEmpty( userEmail )) {
                       Reset.setError( "Enter your registered email id" );
                   } else {
                       auth.sendPasswordResetEmail( userEmail ).addOnCompleteListener( new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()) {
                                   Toast.makeText( getApplicationContext(), "Kindly check your email account", Toast.LENGTH_SHORT ).show();
                                   startActivity( new Intent( getApplicationContext(), Login.class ) );
                               } else {
                                   String message = task.getException().getMessage();
                                   Toast.makeText( getApplicationContext(), "Error Occurred" + message, Toast.LENGTH_SHORT ).show();
                               }
                           }
                       } );
                   }

               }


           } );

       }
       catch (Exception i){
           Toast.makeText( getApplicationContext(),i.getMessage(), Toast.LENGTH_SHORT ).show();
       }
    }
}
