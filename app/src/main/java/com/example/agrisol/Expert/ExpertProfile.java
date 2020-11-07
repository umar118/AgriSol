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
    UserProfileImageRef = FirebaseStorage.getInstance().getReference().child( "Expert Profile Images" );

    UsersRef.addValueEventListener( new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()) {
                if (dataSnapshot.hasChild("expert_Profile")) {
                    String image = dataSnapshot.child("expert_Profile").getValue().toString();
                    Picasso.get().load(image).placeholder(R.drawable.profile).into(ProfileImage);
                }
                 if(dataSnapshot.hasChild( "expert_FullName" )){
                    String expert_FullName = dataSnapshot.child( "expert_FullName" ).getValue().toString();
                    textFullname.setText( expert_FullName );
                }
                 if(dataSnapshot.hasChild( "expert_Email" )){
                    String expert_Email = dataSnapshot.child( "expert_Email" ).getValue().toString();
                    textUseremail.setText( expert_Email );
                }
                if(dataSnapshot.hasChild( "expert_MobileNo" )){
                    String expert_MobileNo = dataSnapshot.child( "expert_MobileNo" ).getValue().toString();
                    textUsermobile.setText( expert_MobileNo );
                }

                if(dataSnapshot.hasChild( "expert_City" )){
                    String expert_City = dataSnapshot.child( "expert_City" ).getValue().toString();
                    textUsercity.setText( expert_City );
                }
                 if(dataSnapshot.hasChild( "expert_Province" )){
                    String expert_Province = dataSnapshot.child( "expert_Province" ).getValue().toString();
                    textUserprovince.setText( expert_Province );
                }
                 if(dataSnapshot.hasChild( "expert_Country" )){
                    String expert_Country = dataSnapshot.child( "expert_Country" ).getValue().toString();
                    textUsercountry.setText( expert_Country );
                }
                if(dataSnapshot.hasChild( "expert_Qualification" )){
                    String expert_Qualification = dataSnapshot.child( "expert_Qualification" ).getValue().toString();
                    textExpertQualification.setText( expert_Qualification );
                }
                 if(dataSnapshot.hasChild( "expert_Expertise" )){

                    String expert_Experties = dataSnapshot.child( "expert_Expertise" ).getValue().toString();
                    textExperties.setText( expert_Experties );

                }

                Experts experts =new Experts(    );



            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText( getContext(),databaseError.getMessage(),Toast.LENGTH_SHORT ).show();
        }
    } );
}
catch (Exception i ){
    Toast.makeText( getContext(),i.getMessage(),Toast.LENGTH_SHORT ).show();
}
    }

}
