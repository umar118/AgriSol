package com.example.agrisolutions;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends Fragment {

    private FirebaseAuth mAuth;
    private DatabaseReference UsersRef;
    String currentUserID;
    private TextView textFullname, textUsername, textUseremail, textUsermobile, textUsercity, textUserprovince, textUsercountry,textUseroccupation;
    private CircleImageView ProfileImage;
    private StorageReference UserProfileImageRef;
    final static int Gallery_Pick = 1;
    private ProgressDialog loadingBar;


    private UserProfileViewModel mViewModel;

    public static UserProfile newInstance() {
        return new UserProfile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("message","onCreateView() of BlankFragment");


        return inflater.inflate(R.layout.user_profile_fragment, container, false);
        //loadingBar = new ProgressDialog(this);




    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);

        ProfileImage = (CircleImageView) getView().findViewById(R.id.profile_image_view);
        textFullname = (TextView) getView().findViewById(R.id.text_userfullname);
        textUsername = (TextView) getView().findViewById(R.id.text_username);
        textUseremail = (TextView) getView().findViewById(R.id.text_useremail);
        textUsermobile = (TextView) getView().findViewById(R.id.text_usermobile);
        textUsercity =  (TextView) getView().findViewById(R.id.text_usercity);
        textUserprovince = (TextView) getView().findViewById(R.id.text_userprovince);
        textUsercountry = (TextView) getView().findViewById(R.id.text_usercountry);
        textUseroccupation = (TextView) getView().findViewById(R.id.text_useroccu);
        // TODO: Use the ViewModel

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("User Profile Images");




        UsersRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("userprofileimage")) {
                        //String image = "https://firebasestorage.googleapis.com/v0/b/poster-44926.appspot.com/o/Profile%20Images%2FjIR4L7pSWphSsBlBT8xu52FXI6L2.jpg?alt=media&token=86f09465-0562-4a00-ae15-1b5d9d94727b";
                        String image = dataSnapshot.child("userprofileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.header_profile).into(ProfileImage);
                    }

                    if (dataSnapshot.hasChild("user_FullName")) {
                        String user_FullName = dataSnapshot.child("user_FullName").getValue().toString();

                        textFullname.setText(user_FullName);
                    }
                    if (dataSnapshot.hasChild("user_UserName")) {
                        String user_UserName = dataSnapshot.child("user_UserName").getValue().toString();

                        textUsername.setText(user_UserName);
                    }
                    if (dataSnapshot.hasChild("user_Email")) {
                        String user_Email = dataSnapshot.child("user_Email").getValue().toString();

                        textUseremail.setText(user_Email);
                    }
                    if (dataSnapshot.hasChild("user_MobileNo")) {
                        String user_MobileNo = dataSnapshot.child("user_MobileNo").getValue().toString();

                        textUsermobile.setText(user_MobileNo);
                    }
                    if (dataSnapshot.hasChild("user_City")) {
                        String user_City= dataSnapshot.child("user_City").getValue().toString();

                        textUsercity.setText(user_City);
                    }
                    if (dataSnapshot.hasChild("user_Province")) {
                        String user_Province = dataSnapshot.child("user_Province").getValue().toString();

                       textUserprovince.setText(user_Province);
                    }
                    if (dataSnapshot.hasChild("user_Country")) {
                        String user_Country = dataSnapshot.child("user_Country").getValue().toString();

                        textUsercountry.setText(user_Country);
                    }
                    if (dataSnapshot.hasChild("user_Occupation")) {
                        String user_Occupation = dataSnapshot.child("user_Occupation").getValue().toString();

                        textUseroccupation.setText(user_Occupation);
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
