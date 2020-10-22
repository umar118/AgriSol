package com.example.agrisol.Expert;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agrisol.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExpertProfile extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private StorageReference UserProfileImageRef;
    private TextView textFullname,textUseremail, textUsermobile, textUsercity, textUserprovince, textUsercountry, textExpertQualification,textExperties;
    private CircleImageView ProfileImage;
    FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.expert_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ProfileImage = (CircleImageView) getView().findViewById(R.id.expertProfileView);
        textFullname = (TextView) getView().findViewById(R.id.text_expert_fullname);
        textUseremail = (TextView) getView().findViewById(R.id.text_email_email);
        textUsermobile = (TextView) getView().findViewById(R.id.text_expert_rmobile);
        textUsercity = (TextView) getView().findViewById(R.id.text_expert_city);
        textUserprovince = (TextView) getView().findViewById(R.id.text_expert_province);
        textUsercountry = (TextView) getView().findViewById(R.id.text_expert_country);
        textExpertQualification = (TextView) getView().findViewById(R.id.text_expert_quali);
        textExperties = (TextView) getView().findViewById(R.id.text_experties);
        floatingActionButton=getView().findViewById(R.id.updateFloatExpert);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExpertProfile.this.getContext(), UpdateExpertProfile.class));
            }
        });
try {


    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    UsersRef = FirebaseDatabase.getInstance().getReference().child( "Users" ).child( "Expert" ).child( user.getUid() );
    UserProfileImageRef = FirebaseStorage.getInstance().getReference().child( "User Profile Images" );

    UsersRef.addValueEventListener( new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()) {

                String image = dataSnapshot.child( "expertprofileimage" ).getValue().toString();
                String expert_FullName = dataSnapshot.child( "expert_FullName" ).getValue().toString();
                String expert_Email = dataSnapshot.child( "expert_Email" ).getValue().toString();
                String expert_MobileNo = dataSnapshot.child( "expert_MobileNo" ).getValue().toString();
                String expert_City = dataSnapshot.child( "expert_City" ).getValue().toString();
                String expert_Province = dataSnapshot.child( "expert_Province" ).getValue().toString();
                String expert_Country = dataSnapshot.child( "expert_Country" ).getValue().toString();
                String expert_Qualification = dataSnapshot.child( "expert_Qualification" ).getValue().toString();
                String expert_Experties = dataSnapshot.child( "expert_Experties" ).getValue().toString();

                textFullname.setText( expert_FullName );
                textUseremail.setText( expert_Email );
                textUsermobile.setText( expert_MobileNo );
                textUsercity.setText( expert_City );
                textUsercountry.setText( expert_Country );
                textUserprovince.setText( expert_Province );
                textExpertQualification.setText( expert_Qualification );
                textExperties.setText( expert_Experties );
                Picasso.get().load( image ).placeholder( R.drawable.profile ).into( ProfileImage );
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    } );
}
catch (Exception i ){
    Toast.makeText( getContext(),i.getMessage(),Toast.LENGTH_SHORT ).show();
}
    }

}
