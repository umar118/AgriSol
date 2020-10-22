package com.example.agrisol.User;

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

import java.util.regex.Pattern;


public class CreateUserAccount extends AppCompatActivity {
    private EditText UserEmail,UserPassword,UserConfirmPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private Button RegisterUser;
    private static final Pattern PASSWORD_PATTERN =Pattern.compile( "^"+"(?=.*[0-9])"+"(?=.*[a-zA-Z])"+"(?=\\S+$)"+".{6,}"+"$" );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_user_account);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Create User Account");

        UserEmail = findViewById(R.id.Edit_UserEmail);
        UserPassword = findViewById(R.id.Edit_UserPassword);
        UserConfirmPassword = findViewById(R.id.Edit_UserConPassword);
        loadingBar = new ProgressDialog(this);

        RegisterUser = findViewById(R.id.Edit_UserAdd);

        mAuth = FirebaseAuth.getInstance();

        RegisterUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateNewAccount();

            }


            private void CreateNewAccount() {

                String User_Email = UserEmail.getText().toString();
                String User_Password = UserPassword.getText().toString();
                String User_ConfirmPassword = UserConfirmPassword.getText().toString();

                if (TextUtils.isEmpty(User_Email)) {
                    UserEmail.setError( "Please Enter Email" );
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher( User_Email ).matches()){
                    UserEmail.setError( "Please Enter Valid Email" );
                }
                else if (TextUtils.isEmpty(User_Password)) {
                   UserPassword.setError( "Please Enter Password" );
                }
                else if(!PASSWORD_PATTERN.matcher( User_Password ).matches()){
                    UserPassword.setError( "Weak Password" );
                }
                else if (TextUtils.isEmpty(User_ConfirmPassword)) {
                    UserConfirmPassword.setError( "Please Enter Confirm Password" );
                } else if (!User_Password.equals(User_ConfirmPassword)) {
                    UserConfirmPassword.setError( "Entered Password Not Be Match" );
                } else {
                    loadingBar.setTitle("Creating New Account");
                    loadingBar.setMessage("Please Wait, while we are creating your Account...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);

                    mAuth.createUserWithEmailAndPassword(User_Email, User_Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SendUserToSetupActivity();
                                Toast.makeText(CreateUserAccount.this, "you are authenticated succesfully...", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(CreateUserAccount.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }
                        }

                        private void SendUserToSetupActivity() {

                            Intent setupIntent = new Intent(CreateUserAccount.this, RegisterUserProfile.class);
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
