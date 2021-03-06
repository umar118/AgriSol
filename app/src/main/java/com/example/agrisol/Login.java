package com.example.agrisol;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.agrisol.Admin.AdminLogin;
import com.example.agrisol.Expert.CreateExpertAccount;
import com.example.agrisol.Expert.ExpertDashboard;
import com.example.agrisol.User.CreateUserAccount;
import com.example.agrisol.User.UserDashboard;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity {
    TextView tvCreateNewAccount;
    EditText edtLoginEmail, edtLoginPassword;
    RadioButton radiobtnUser, radiobtnExpert;
    CoordinatorLayout coordinatorLayout;
    RadioGroup radioGroup;
    Button btnLogin;
    FirebaseAuth mAuth;
    ProgressDialog loadingBar;
    DatabaseReference reference;
    int category_val;
    private FirebaseAuth.AuthStateListener firebaseAuthlistner;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    TextView forgetPassword;
    private AlertDialog alertDialog;
    private ImageView adminLogin;
    private static final Pattern PASSWORD_PATTERN =Pattern.compile( "^"+"(?=.*[0-9])"+"(?=.*[a-zA-Z])"+"(?=\\S+$)"+".{6,}"+"$" );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        adminLogin=findViewById(R.id.admin);
        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminLogin.class));
                finish();
            }
        });

        coordinatorLayout =findViewById(R.id.coordinatorLayout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Login Here");
        mAuth = FirebaseAuth.getInstance();
        final SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

        firebaseAuthlistner = new FirebaseAuth.AuthStateListener() {
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    category_val = prefs.getInt("Val", 0);
                    System.out.println("My Saved Id : " + category_val);
                    if (category_val == 1) {
                        Intent intent = new Intent(Login.this, UserDashboard.class);
                        startActivity(intent);
                        finish();
                    }

                    if (category_val == 2) {
                        Intent intent = new Intent(Login.this, ExpertDashboard.class);
                        startActivity(intent);
                        finish();
                    }
                    return;
                }
            }
        };


        initialize();

        tvCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
                View v = inflater.inflate(R.layout.ask_dialog_create, null);

                Button btnUser = v.findViewById(R.id.user);
                Button btnExpert = v.findViewById(R.id.expert);

                btnUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), CreateUserAccount.class));

                    }
                });
                btnExpert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), CreateExpertAccount.class));


                    }
                });

                alertDialog = new AlertDialog.Builder(Login.this)
                        .setView(v)
                        .create();

                alertDialog.show();
            }
        });



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allowingUserToLogin();
            }
        });
    }

    private void initialize() {
        tvCreateNewAccount = findViewById(R.id.createAccount);
        edtLoginEmail = findViewById(R.id.editEmail);
        edtLoginPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.loginButton);
        radiobtnUser = findViewById(R.id.Userid);
        radiobtnExpert = findViewById(R.id.Expertid);
        loadingBar = new ProgressDialog(this);
        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        forgetPassword = findViewById(R.id.forgetPass);
        mAuth = FirebaseAuth.getInstance();
    }


    @SuppressLint("ResourceType")

    private void allowingUserToLogin() {

        final String email = edtLoginEmail.getText().toString().trim();
        final String password = edtLoginPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            edtLoginEmail.setError("Please Enter Email");
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher( email ).matches()){
            edtLoginEmail.setError( "Please Enter Valid Email" );
        }

        else if (TextUtils.isEmpty(password)) {
            edtLoginPassword.setError("Please Enter Password");
        }
        else if(!PASSWORD_PATTERN.matcher( password ).matches()){
            edtLoginPassword.setError( "Weak Password" );
        }
        else if (radioGroup.getCheckedRadioButtonId() <= 0) {
            Snackbar snackbar = Snackbar.make(coordinatorLayout,"Please Select Account",Snackbar.LENGTH_SHORT);
            snackbar.show();
            radiobtnExpert.setError("Select item please");
        } else {
            loadingBar.setTitle("Logging In");
            loadingBar.setMessage("Please Wait while we are logging in you");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if (radiobtnUser.isChecked()) {
                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child("User");
                            reference.orderByChild("user_Email").equalTo(email).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        loadingBar.dismiss();
                                        edtLoginEmail.setText("");
                                        edtLoginPassword.setText("");
                                        SendToUserDashboard();
                                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                        editor.putString("User", "done");
                                        editor.apply();

                                    } else {
                                        Snackbar snackbar = Snackbar.make(coordinatorLayout,"This Email Does Not Exist In This Category ",Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                        loadingBar.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {

                            reference = FirebaseDatabase.getInstance().getReference().child("Users").child("Expert");
                            reference.orderByChild("expert_Email").equalTo(email).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        loadingBar.dismiss();
                                        edtLoginEmail.setText("");
                                        edtLoginPassword.setText("");
                                       SendToExpertDashboard();
                                        SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                        editor.putString("Expert", "done");
                                        editor.apply();
                                    } else {
                                        Snackbar snackbar = Snackbar.make(coordinatorLayout,"This Email Does Not Exist In This Category ",Snackbar.LENGTH_SHORT);
                                        snackbar.show();
                                        loadingBar.dismiss();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Snackbar snackbar = Snackbar.make(coordinatorLayout,"DataBase Error:  "+databaseError,Snackbar.LENGTH_SHORT);
                                    snackbar.show();
                                }
                            });
                        }
                    } else {
                        String message = task.getException().getMessage();
                        Snackbar snackbar = Snackbar.make(coordinatorLayout,"Error: "+message,Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(firebaseAuthlistner);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(firebaseAuthlistner);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.cancel();
        }
    }


    public void PasswordForget(View view){
        TextView pass =findViewById(R.id.forgetPass);
        startActivity(new Intent(Login.this, ResetPassword.class));
        finish();
    }

    private void SendToUserDashboard(){
        Intent intent = new Intent(getApplicationContext(), UserDashboard.class);
        startActivity(intent);
        finish();
    }

    private  void SendToExpertDashboard(){
        Intent intent = new Intent(getApplicationContext(), ExpertDashboard.class);
        startActivity(intent);
        finish();
    }
}
