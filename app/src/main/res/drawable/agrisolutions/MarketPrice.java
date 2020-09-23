package com.example.agrisolutions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MarketPrice extends Fragment {

    private MarketPriceViewModel mViewModel;

    private FirebaseAuth mAuth;
    private DatabaseReference AdminRef;
    String AdminId;
    private TextView textSerial, textCropName, textCropPrice, textCropDivision;


    public static MarketPrice newInstance() {
        return new MarketPrice();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.market_price_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MarketPriceViewModel.class);

        // TODO: Use the ViewModel


        textSerial = (TextView) getView().findViewById(R.id.serial_no);
        textCropName = (TextView) getView().findViewById(R.id.Crop_name);
        textCropPrice = (TextView) getView().findViewById(R.id.price_today);
        textCropDivision = (TextView) getView().findViewById(R.id.division);

        mAuth = FirebaseAuth.getInstance();
        AdminId = mAuth.getCurrentUser().getUid();
        AdminRef = FirebaseDatabase.getInstance().getReference().child("MarketRates").child(AdminId);


        mAuth = FirebaseAuth.getInstance();
        AdminId = mAuth.getCurrentUser().getUid();



        AdminRef.child(AdminId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    if (dataSnapshot.hasChild("serialNo")) {
                        String serialNo = dataSnapshot.child("serialNo").getValue().toString();

                        textSerial.setText(serialNo);
                    }
                    if (dataSnapshot.hasChild("cropName")) {
                        String cropName = dataSnapshot.child("cropName").getValue().toString();

                        textCropName.setText(cropName);
                    }
                    if (dataSnapshot.hasChild("cropPrice")) {
                        String cropPrice = dataSnapshot.child("cropPrice").getValue().toString();

                        textCropPrice.setText(cropPrice);
                    }
                    if (dataSnapshot.hasChild("cropDivision")) {
                        String cropDivision = dataSnapshot.child("cropDivision").getValue().toString();

                        textCropDivision.setText(cropDivision);
                    }

                    else {
                        //Toast.makeText(ProfileActivity.this, "Profile name do not exists...", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }



}
