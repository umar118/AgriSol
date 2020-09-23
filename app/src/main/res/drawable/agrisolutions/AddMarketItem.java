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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddMarketItem extends AppCompatActivity {

    private EditText Editserial,Editcropname,EditcropPrice,Editcropdivision;
    private FirebaseAuth mAuth;
    private DatabaseReference AdminRef;
    private ProgressDialog loadingBar;
    private Button Additem;
    String AdminId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_market_item);

        mAuth = FirebaseAuth.getInstance();
        AdminId = mAuth.getCurrentUser().getUid();
        AdminRef = FirebaseDatabase.getInstance().getReference().child("MarketRates").child(AdminId);

        Toolbar toolbar = findViewById(R.id.toolbarid);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Add Market Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),UserDashboard.class));
                finish();
            }
        });


        Editserial = (EditText) findViewById(R.id.Serial);
        Editcropname=(EditText) findViewById(R.id.CropName);
        EditcropPrice=(EditText) findViewById(R.id.CropPrice);
        Editcropdivision=(EditText) findViewById(R.id.CropDivision);


        loadingBar= new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        Additem = (Button) findViewById(R.id.EditAdditem);
        Additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveAccountSetupinformation();
            }


        });
    }


    private void saveAccountSetupinformation() {

        String serialno = Editserial.getText().toString();
        String crop_name = Editcropname.getText().toString();
        String cpropprice = EditcropPrice.getText().toString();
        String cropdivision = Editcropdivision.getText().toString();

        if(TextUtils.isEmpty(serialno))
        {
            Toast.makeText(AddMarketItem.this, "Please write serial number...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(crop_name))
        {
            Toast.makeText(AddMarketItem.this, "Please write crop name...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cpropprice))
        {
            Toast.makeText(AddMarketItem.this, "Please write crop price...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cropdivision))
        {
            Toast.makeText(AddMarketItem.this, "Please write division...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please Wait, while we are add item ...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("serialNo",serialno);
            userMap.put("cropName",crop_name);
            userMap.put("cropPrice",cpropprice);
            userMap.put("cropDivision",cropdivision);

            AdminRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                    if ( task.isSuccessful())
                    {

                        Toast.makeText(AddMarketItem.this, "your data add succesfully", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();

                    }
                    else
                    {
                        String message = task.getException().getMessage();
                        Toast.makeText(AddMarketItem.this, "Error occured: "+message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }
            });
        }
    }


}
