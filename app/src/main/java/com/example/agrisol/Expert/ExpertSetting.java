package com.example.agrisol.Expert;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agrisol.Login;
import com.example.agrisol.R;
import com.google.firebase.auth.FirebaseAuth;

public class ExpertSetting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expert_setting);


        TextView update =findViewById(R.id.update_expert);
        TextView sign=findViewById(R.id.expert_signout);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(), UpdateExpertProfile.class));
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth  mAuth = FirebaseAuth.getInstance();
                mAuth.signOut();
                senduserTologinActivity();
            }
        });

    }
    private void senduserTologinActivity() {

        Intent loginIntent = new Intent(getApplicationContext(), Login.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}
