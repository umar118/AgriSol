package com.example.agrisol.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.agrisol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class AddMarketList extends AppCompatActivity {


    private EditText CropName,CropPrice,CropDistrict,CurrentDate;
    private DatabaseReference PriceRef;
    private ProgressDialog loadingBar;
    private CardView AddCrop;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_market_list);

        CropName = findViewById(R.id.Edit_CropName);
        CropPrice =findViewById(R.id.Edit_price);
        CropDistrict =findViewById(R.id.Edit_district);
        CurrentDate =findViewById(R.id.Edit_current_date);

        AddCrop =findViewById(R.id.Add_List);
        AddCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
AddList();
            }
        });


        PriceRef = FirebaseDatabase.getInstance().getReference().child("Market Rate").push();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Add Crop's Price");

        loadingBar= new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

    }

    private void AddList() {

        final String Crop_Name = CropName.getText().toString().trim();
        final String Crop_Price = CropPrice.getText().toString().trim();
        final String Crop_District = CropDistrict.getText().toString().trim();
        final String Crop_Date = CurrentDate.getText().toString().trim();

        if (TextUtils.isEmpty(Crop_Name)) {
            Toast.makeText(AddMarketList.this, "Please Write Crop's Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Crop_Price)) {
            Toast.makeText(AddMarketList.this, "Please Write Crop's Price", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Crop_District)) {
            Toast.makeText(AddMarketList.this, "Please Write District", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Crop_Date)) {
            Toast.makeText(AddMarketList.this, "Please Write Date", Toast.LENGTH_SHORT).show();
        }
        else

        {
            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("wait for adding data...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap crop_map = new HashMap();
            crop_map.put("CropName", Crop_Name);
            crop_map.put("CropPrice", Crop_Price);
            crop_map.put("CropDistrict", Crop_District);
            crop_map.put("Current_Date",Crop_Date);

            PriceRef.updateChildren(crop_map).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(AddMarketList.this, "your data add successfully", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                        Clear();
                        startActivity(new Intent(getApplicationContext(), AdminDashboard.class));
                        finish();

                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(AddMarketList.this, "Error occured: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }

                }
            });
        }
    }

public void Clear(){
        CropName.getText().clear();
        CropPrice.getText().clear();
        CropDistrict.getText().clear();
}

}
