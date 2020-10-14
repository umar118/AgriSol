package com.example.agrisol.Market;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.agrisol.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UpdatePrice extends AppCompatActivity {


  private   EditText Name,Price,Dis,Date;
  private   CardView delete,edit;
  private   DatabaseReference Ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_price);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UpdateMarketList.class));
                finish();
            }
        });

        Ref = FirebaseDatabase.getInstance().getReference().child("Market Rate");

        Name = (EditText) findViewById(R.id.Update_CropName);
        Price = findViewById(R.id.Update_CropPrice);
        Dis = findViewById(R.id.Update_CropDistrict);
        Date = findViewById(R.id.Update_current_date);


        Ref.child("Market Rate");
        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    if (dataSnapshot.hasChild("CropName")){
                        String cropName = dataSnapshot.child("CropName").getValue().toString();
                        Name.setText(cropName);
                    }
                    if (dataSnapshot.hasChild("CropPrice")){
                        String cropPrice = dataSnapshot.child("CropPrice").getValue().toString();
                        Price.setText(cropPrice);
                    }
                    if(dataSnapshot.hasChild("CropDistrict")){
                        String cropDist = dataSnapshot.child("CropDistrict").getValue().toString();
                        Dis.setText(cropDist);
                    }
                    if (dataSnapshot.hasChild("Current_Date")) {
                        String cropDate = dataSnapshot.child("Current_Date").getValue().toString();
                        Date.setText(cropDate);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
