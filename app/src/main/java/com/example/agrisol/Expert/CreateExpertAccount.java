package com.example.agrisol.Expert;

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

public class CreateExpertAccount extends AppCompatActivity {
    private EditText ExpertEmail,ExpertPassword,ExpertConfirmPassword;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;
    private Button RegisterExpert;
    private static final Pattern PASSWORD_PATTERN =Pattern.compile( "^"+"(?=.*[0-9])"+"(?=.*[a-zA-Z])"+"(?=\\S+$)"+".{6,}"+"$" );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_expert_account);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Create Expert Account");

        ExpertEmail = findViewById(R.id.Edit_ExpertEmail);
        ExpertPassword = findViewById(R.id.Edit_ExpertPassword);
        ExpertConfirmPassword = findViewById(R.id.Edit_ExpertConPassword);
        loadingBar = new ProgressDialog(this);

        RegisterExpert = findViewById(R.id.Edit_ExpertAdd);

        mAuth = FirebaseAuth.getInstance();

        RegisterExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateNewAccount();

            }

            private void CreateNewAccount() {

                String Expert_Email = ExpertEmail.getText().toString();
                String Expert_Password = ExpertPassword.getText().toString();
                String Expert_ConfirmPassword = ExpertConfirmPassword.getText().toString();

                if (TextUtils.isEmpty(Expert_Email)) {
                   ExpertEmail.setError( "Please Enter Email" );
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher( Expert_Email ).matches()){
                    ExpertEmail.setError( "Please Enter Valid Email" );
                }
                else if (TextUtils.isEmpty(Expert_Password)) {
                    ExpertPassword.setError( "Please Enter Password" );
                }
                else if(!PASSWORD_PATTERN.matcher( Expert_Password ).matches()){
                    ExpertPassword.setError( "Weak Password" );
                }
                else if (TextUtils.isEmpty(Expert_ConfirmPassword)) {
                    ExpertConfirmPassword.setError( "Enter Confirm Password" );
                } else if (!Expert_Password.equals(Expert_ConfirmPassword)) {
                    ExpertConfirmPassword.setError( "Entered Password Not Be Match" );
                } else {
                    loadingBar.setTitle("Creating New Account");
                    loadingBar.setMessage("Please Wait, while we are creating your Account...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(true);

                    mAuth.createUserWithEmailAndPassword(Expert_Email, Expert_Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                SendUserToSetupActivity();
                                loadingBar.dismiss();

                            } else {
                                String message = task.getException().getMessage();
                                Toast.makeText(CreateExpertAccount.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                            }
                        }

                        private void SendUserToSetupActivity() {

                            Intent setupIntent = new Intent(CreateExpertAccount.this, RegisterExpertProfile.class);
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
