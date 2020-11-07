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

import com.example.agrisol.Login.Login;
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

public class RegisterUserProfile extends AppCompatActivity {

    private EditText  FullName, Email,City, Province,Country,Occupation,Mobile;
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
        setContentView(R.layout.activity_register_user_profile);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Register Profile");


        loadingBar= new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
    //    currentUserID = mAuth;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child("User").child(user.getUid());

        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("User Profile Images").child(user.getUid());;

      //  Username = (EditText) findViewById(R.id.UserName);
        FullName = (EditText) findViewById(R.id.UserFullName);
        Email = (EditText) findViewById(R.id.UserEmail);
        Mobile= (EditText) findViewById(R.id.UserMobile);
        City =(EditText) findViewById(R.id.UserCity);
        Province =(EditText) findViewById(R.id.UserProvince);
        Country=(EditText) findViewById(R.id.UserCountry);
        Occupation=(EditText) findViewById(R.id.UserOccupation);
       // Password=(EditText) findViewById(R.id.UserPassword);

        SaveInformationbutton = (Button) findViewById(R.id.UserCreate);
        SaveInformationbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateUser();
            }


        });


        ProfileImage = (CircleImageView) findViewById(R.id.CreateUserProfile);


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
                    else
                    {
                        Toast.makeText(RegisterUserProfile.this, "Please select profile image first.", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(RegisterUserProfile.this, "Profile Image stored successfully to Firebase storage...", Toast.LENGTH_SHORT).show();
                            final String downloadUrl = downUri.toString();
                            UserRef.child("userprofileimage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent selfIntent = new Intent(RegisterUserProfile.this,RegisterUserProfile.class);
                                                startActivity(selfIntent);

                                                Toast.makeText(RegisterUserProfile.this, "Profile Image stored to Firebase Database Successfully...", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            } else {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(RegisterUserProfile.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
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

    public void CreateUser() {

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
            loadingBar.setTitle("Authenticating");
            loadingBar.setMessage("Please Wait, while we are your new account...");
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
            loadingBar.setMessage("Creating Your Account");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);

            UserRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(RegisterUserProfile.this, "Account Created Succesfully.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        startActivity(new Intent(getApplicationContext(), UserDashboard.class));
                        finish();
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(RegisterUserProfile.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });

        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

}