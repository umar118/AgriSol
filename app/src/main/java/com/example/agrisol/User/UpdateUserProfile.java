package com.example.agrisol.User;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.agrisol.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateUserProfile extends AppCompatActivity {

    private EditText FullName, Email,City, Province,Country,Occupation,Mobile;
    private Button SaveInformationbutton;
    private CircleImageView ProfileImage;
    private FirebaseAuth mAuth;
    private DatabaseReference UserRef;
    private ProgressDialog loadingBar;
    String currentUserID;
    private StorageReference UserProfileImageRef;
    final static int Gallery_Pick =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_user_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Update Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                finish();
            }
        });

        loadingBar= new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child("User").child(user.getUid());

        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("User Profile Images").child(user.getUid());;

        FullName = (EditText) findViewById(R.id.update_UserFullName);
        Email = (EditText) findViewById(R.id.update_UserEmail);
        Mobile= (EditText) findViewById(R.id.update_UserMobile);
        City =(EditText) findViewById(R.id.update_UserCity);
        Province =(EditText) findViewById(R.id.update_UserProvince);
        Country=(EditText) findViewById(R.id.update_UserCountry);
        Occupation=(EditText) findViewById(R.id.update_UserOccupation);

        SaveInformationbutton = (Button) findViewById(R.id.UserUpdate);
        SaveInformationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

UpdateUser();

            }


        });

        ProfileImage = (CircleImageView) findViewById(R.id.UpdateUserProfile);


        ProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                // startActivityForResult(galleryIntent, Gallery_Pick);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"),Gallery_Pick);
            }
        });


        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("userprofileimage"))
                    {
                        String image = dataSnapshot.child("userprofileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(ProfileImage);
                    }
                    if (dataSnapshot.hasChild("user_FullName"))
                    {
                        String user_FullName = dataSnapshot.child("user_FullName").getValue().toString();
                        FullName.setText(user_FullName);
                    }
                    if (dataSnapshot.hasChild("user_Email"))
                    {
                        String user_Email = dataSnapshot.child("user_Email").getValue().toString();
                        Email.setText(user_Email);
                    }
                    if (dataSnapshot.hasChild("user_MobileNo"))
                    {
                        String user_MobileNo = dataSnapshot.child("user_MobileNo").getValue().toString();
                        Mobile.setText(user_MobileNo);
                    }
                    if (dataSnapshot.hasChild("user_Occupation"))
                    {
                        String user_Occupation = dataSnapshot.child("user_Occupation").getValue().toString();
                        Occupation.setText(user_Occupation);
                    }
                    if (dataSnapshot.hasChild("user_City"))
                    {
                        String user_City = dataSnapshot.child("user_City").getValue().toString();
                        City.setText(user_City);
                    }
                    if (dataSnapshot.hasChild("user_Province"))
                    {
                        String user_Province = dataSnapshot.child("user_Province").getValue().toString();
                        Province.setText(user_Province);
                    }
                    if (dataSnapshot.hasChild("user_Country"))
                    {
                        String user_Country = dataSnapshot.child("user_Country").getValue().toString();
                        Country.setText(user_Country);
                    }
                    else
                    {
                        Toast.makeText(UpdateUserProfile.this, "Please select profile image first.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        UserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("userprofileimage"))
                    {
                        String image = dataSnapshot.child("userprofileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(ProfileImage);
                    }
                    else
                    {
                        Toast.makeText(UpdateUserProfile.this, "Please select profile image first.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();

            CropImage.activity(ImageUri)
                    .setGuidelines(CropImageView.Guidelines.ON).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                loadingBar.setTitle("Profile Image");
                loadingBar.setMessage("Please wait, while we updating your profile image...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(true);

                Uri resultUri = result.getUri();

                final StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg");

                filePath.putFile(resultUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downUri = task.getResult();
                            Toast.makeText(UpdateUserProfile.this, "Profile Image stored successfully to Firebase storage...", Toast.LENGTH_SHORT).show();
                            final String downloadUrl = downUri.toString();
                            UserRef.child("userprofileimage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent selfIntent = new Intent(UpdateUserProfile.this,UpdateUserProfile.class);
                                                startActivity(selfIntent);

                                                Toast.makeText(UpdateUserProfile.this, "Profile Image stored to Firebase Database Successfully...", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            } else {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(UpdateUserProfile.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });
                        }

                    }

                });

            }
        }
    }

    public void UpdateUser() {

        final   String User_Fullname = FullName.getText().toString().trim();
        final   String User_Email = Email.getText().toString().trim();
        final   String User_Mobile_no = Mobile.getText().toString().trim();
        final   String User_City = City.getText().toString().trim();
        final   String User_Province= Province.getText().toString().trim();
        final   String User_Country= Country.getText().toString().trim();
        final   String User_Occupation= Occupation.getText().toString().trim();

        if(TextUtils.isEmpty(User_Fullname))
        {
            FullName.setError( "Enter Name" );
        }
        else if(TextUtils.isEmpty(User_Email))
        {
            Email.setError( "Enter Registered Email Address" );
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher( User_Email ).matches()){
            Email.setError( "Please Enter Valid Email" );
        }
        else if(TextUtils.isEmpty(User_Mobile_no))
        {
            Mobile.setError( "Enter Mobile Number" );
        }
        else if (!Patterns.PHONE.matcher( User_Mobile_no ).matches()){
            Mobile.setError( "Enter Valid Mobile Number" );
        }
        else if(TextUtils.isEmpty(User_Occupation))
        {
            Occupation.setError( "Enter Occupation" );
        }
        else if(TextUtils.isEmpty(User_City))
        {
            City.setError( "Enter City Name" );
        }
        else if(TextUtils.isEmpty(User_Province))
        {
            Province.setError( "Enter Province Name" );
        }
        else if(TextUtils.isEmpty(User_Country))
        {
            Country.setError( "Enter Country" );
        }

        else
        {
            loadingBar.setTitle("Updating");
            loadingBar.setMessage("Please Wait,your is updating");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();

            userMap.put("user_FullName",User_Fullname);
            userMap.put("user_Email",User_Email);
            userMap.put("user_MobileNo",User_Mobile_no);
            userMap.put("user_Occupation",User_Occupation);
            userMap.put("user_City",User_City);
            userMap.put("user_Province",User_Province);
            userMap.put("user_Country",User_Country);

            loadingBar.dismiss();
            loadingBar.setMessage("Update Account");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);

            UserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(UpdateUserProfile.this, "Account Updated Successfully.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        startActivity(new Intent(getApplicationContext(), UpdateUserProfile.class));
                        finish();
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(UpdateUserProfile.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });


        }}
}
