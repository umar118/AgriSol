package com.example.agrisol.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.agrisol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class CreateAdminAccount extends AppCompatActivity {


    EditText AdminEmail,AdminPassword,AdminName,AdminMobile;
    Button createAdmin;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private static final Pattern PASSWORD_PATTERN =Pattern.compile( "^"+"(?=.*[0-9])"+"(?=.*[a-zA-Z])"+"(?=\\S+$)"+".{6,}"+"$" );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_admin_account);

        AdminEmail =findViewById(R.id.Edit_AdminEmail);
        AdminPassword =findViewById(R.id.Edit_AdminPassword);
        AdminName =findViewById(R.id.Edit_AdminName);
        AdminMobile =findViewById(R.id.Edit_AdminMobile);
        loadingBar = new ProgressDialog(this);
        createAdmin=findViewById(R.id.Edit_ExpertAdd);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Create Admin Account");

        mAuth = FirebaseAuth.getInstance();

        createAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateNewAccount();
            }


    private void CreateNewAccount() {

      final   String Admin_Email = AdminEmail.getText().toString().trim();
      final   String Admin_Password = AdminPassword.getText().toString().trim();
      final   String Admin_Name = AdminName.getText().toString().trim();
      final   String Admin_Mobile = AdminMobile.getText().toString().trim();

        if (TextUtils.isEmpty(Admin_Email)) {
            AdminEmail.setError( "Please Enter Email" );
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher( Admin_Email ).matches()){
            AdminEmail.setError( "Please Enter Valid Email" );
        }
        else if (TextUtils.isEmpty(Admin_Password)) {
            AdminPassword.setError( "Please Enter Password" );
        }
        else if(!PASSWORD_PATTERN.matcher( Admin_Password ).matches()){
            AdminPassword.setError( "Weak Password" );
        }
        else if (TextUtils.isEmpty(Admin_Name)) {
           AdminName.setError( "Please Enter Name" );
        } else if (TextUtils.isEmpty(Admin_Mobile)) {
           AdminMobile.setError( "Please Enter Mobile Number" );
        } else {
            loadingBar.setTitle("Creating New Account");
            loadingBar.setMessage("Please Wait, while we are creating your Account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.createUserWithEmailAndPassword(Admin_Email, Admin_Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        String current_user_id;
                        final DatabaseReference customerRef;
                        current_user_id = mAuth.getCurrentUser().getUid();
                        customerRef = FirebaseDatabase.getInstance().getReference().child("Admin").child(current_user_id);

                        loadingBar.dismiss();
                        loadingBar.setMessage("Creating Your Account");
                        loadingBar.show();
                        loadingBar.setCanceledOnTouchOutside(false);

                        HashMap adminMap = new HashMap();
                        adminMap.put("Admin_Email", Admin_Email);
                        adminMap.put("Admin_Name", Admin_Name);
                        adminMap.put("Admin_Mobile", Admin_Mobile);

                        customerRef.updateChildren(adminMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(CreateAdminAccount.this, "Account Created Succesfully.", Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                    startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
                                    finish();
                                } else {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(CreateAdminAccount.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                                    loadingBar.dismiss();
                                }
                            }
                        });

                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(CreateAdminAccount.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();

                    }
                }

                private void SendUserToSetupActivity() {

                    Intent setupIntent = new Intent(CreateAdminAccount.this, AdminDashboard.class);
                    setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(setupIntent);
                    finish();

                }
            });

        }


    }

});


        }

}
