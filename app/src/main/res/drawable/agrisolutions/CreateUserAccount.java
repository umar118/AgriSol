package com.example.agrisolutions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CreateUserAccount extends AppCompatActivity {

    EditText useremail, password, confirm_pass;
    private FirebaseAuth mAuth;
    private Button createuser;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user_account);
        Toolbar toolbar = findViewById(R.id.toolbarid);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Sign Up");


        mAuth = FirebaseAuth.getInstance();
        useremail = findViewById(R.id.Useremail);
        password = findViewById(R.id.UserPassword);
        confirm_pass = findViewById(R.id.UserConfirmPass);
        loadingBar = new ProgressDialog(this);

        createuser = findViewById(R.id.signup_user);
        createuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                CreateNewAccount();





    }

    private void CreateNewAccount() {

            String email = useremail.getText().toString();
            String password_num = password.getText().toString();
            String confirmPassword = confirm_pass.getText().toString();


            if (TextUtils.isEmpty(email)) {
                Toast.makeText(CreateUserAccount.this, "Please write your email...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(password_num)) {
                Toast.makeText(CreateUserAccount.this, "Please write your password...", Toast.LENGTH_SHORT).show();
            } else if (TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(CreateUserAccount.this, "Please confirm your email", Toast.LENGTH_SHORT).show();
            } else if (!password_num.equals(confirmPassword)) {
                Toast.makeText(CreateUserAccount.this, "Your password does not match...", Toast.LENGTH_SHORT).show();
            } else {
                loadingBar.setTitle("Creating New Account");
                loadingBar.setMessage("Please Wait, while we are creating your Account...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                mAuth.createUserWithEmailAndPassword(email, password_num).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            String message = task.getException().getMessage();
                            Toast.makeText(CreateUserAccount.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        } else {
                            SendUserToSetupActivity();
                            Toast.makeText(CreateUserAccount.this, "you are authenticated succesfully...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                        }
                    }

                    private void SendUserToSetupActivity() {

                        Intent setupIntent = new Intent(CreateUserAccount.this, RegisterUser.class);
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









