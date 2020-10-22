package com.example.agrisol.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.agrisol.MainActivity;
import com.example.agrisol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class AdminLogin extends AppCompatActivity {

    EditText adminEmail,adminPassword;
    Button admin;
    FirebaseAuth mAuth;
    ProgressDialog loadingBar;
    ImageView adminCreate;
    private static final Pattern PASSWORD_PATTERN =Pattern.compile( "^"+"(?=.*[0-9])"+"(?=.*[a-zA-Z])"+"(?=\\S+$)"+".{6,}"+"$" );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Admin Login Here");
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(AdminLogin.this, AdminDashboard.class));
            finish();
        }

        adminCreate =findViewById(R.id.admin_create);
        adminCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateAdminAccount.class));
            }
        });

        adminEmail =findViewById(R.id.Admin_Email);
        adminPassword =findViewById(R.id.Admin_Password);
        admin =findViewById(R.id.admin_login);
        loadingBar = new ProgressDialog(this);
        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AdminLogin();
            }

            public void AdminLogin(){


                final String Admin_Email = adminEmail.getText().toString();
                final String Admin_Password = adminPassword.getText().toString();

                if(TextUtils.isEmpty(Admin_Email))
                {
                    adminEmail.setError( "Please Enter Email" );
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher( Admin_Email ).matches()){
                    adminEmail.setError( "Please Enter Valid Email" );
                }
                else  if(TextUtils.isEmpty(Admin_Password))
                {
                    adminPassword.setError( "Please Enter Password" );
                }
                else if(!PASSWORD_PATTERN.matcher( Admin_Password ).matches()){
                    adminPassword.setError( "Weak Password" );
                }
                else
                {
                    loadingBar.setTitle("Login");
                    loadingBar.setMessage("Please Wait, while we are login into your Account...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);

                    mAuth.signInWithEmailAndPassword(Admin_Email,Admin_Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful())
                                    {
                                       DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Admin");
                                        reference.orderByChild("Admin_Email").equalTo(Admin_Email).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    Toast.makeText(AdminLogin.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();
                                                    adminEmail.setText("");
                                                    adminPassword.setText("");
                                                    SendUserToMainActivity();
                                                } else {
                                                    Toast.makeText(AdminLogin.this, "This email doesnot exist in this category", Toast.LENGTH_SHORT).show();
                                                    loadingBar.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });


                                        Toast.makeText(AdminLogin.this, "You are login sucessfully...", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else
                                    {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(AdminLogin.this, "Error occured: "+message, Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }

                                }
                            });
                }



            }
        });
    }

    private void SendUserToMainActivity() {

        Intent mainIntent = new Intent(AdminLogin.this, MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null)
        {
            Intent mainIntent = new Intent(AdminLogin.this,AdminDashboard.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
            finish();
        }
    }



}
