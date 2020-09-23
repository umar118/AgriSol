package com.example.agrisol;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class UserProfile extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    private StorageReference UserProfileImageRef;
    private TextView textFullname,textUseremail, textUsermobile, textUsercity, textUserprovince, textUsercountry, textUseroccupation;
    private CircleImageView ProfileImage;
    FloatingActionButton floatingActionButton;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_profile, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ProfileImage = (CircleImageView) getView().findViewById(R.id.userprofileView);
        textFullname = (TextView) getView().findViewById(R.id.text_userfullname);
        textUseremail = (TextView) getView().findViewById(R.id.text_useremail);
        textUsermobile = (TextView) getView().findViewById(R.id.text_usermobile);
        textUsercity = (TextView) getView().findViewById(R.id.text_usercity);
        textUserprovince = (TextView) getView().findViewById(R.id.text_userprovince);
        textUsercountry = (TextView) getView().findViewById(R.id.text_usercountry);
        textUseroccupation = (TextView) getView().findViewById(R.id.text_useroccu);
        floatingActionButton=getView().findViewById(R.id.updateFloat);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfile.this.getContext(),UpdateUserProfile.class));
            }
        });


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child("User").child(user.getUid());
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("User Profile Images");

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {



                  String image = dataSnapshot.child("userprofileimage").getValue().toString();
                    String user_FullName = dataSnapshot.child("user_FullName").getValue().toString();
                    String user_Email = dataSnapshot.child("user_Email").getValue().toString();
                    String user_MobileNo = dataSnapshot.child("user_MobileNo").getValue().toString();
                    String user_City = dataSnapshot.child("user_City").getValue().toString();
                    String user_Province = dataSnapshot.child("user_Province").getValue().toString();
                    String user_Country = dataSnapshot.child("user_Country").getValue().toString();
                    String user_Occupation = dataSnapshot.child("user_Occupation").getValue().toString();

                    textFullname.setText(user_FullName);
                    textUseremail.setText(user_Email);
                    textUsermobile.setText(user_MobileNo);
                    textUsercity.setText(user_City);
                    textUsercountry.setText(user_Country);
                    textUserprovince.setText(user_Province);
                    textUseroccupation.setText(user_Occupation);
                   Picasso.get().load(image).placeholder(R.drawable.profile).into(ProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


}
