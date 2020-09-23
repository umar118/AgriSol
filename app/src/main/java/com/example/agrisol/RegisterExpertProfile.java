package com.example.agrisol;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

public class RegisterExpertProfile extends AppCompatActivity {
    private EditText ExpertFullName, ExpertEmail,ExpertCity, ExpertProvince,ExpertCountry,ExpertQualification,ExpertMobile,ExpertExperties,ExpertUniversity;
    private FirebaseAuth mAuth;
    private CircleImageView ExpertProfile;
    private DatabaseReference UsersRef;
    private ProgressDialog loadingBar;
    String currentExpertID;
    private StorageReference ExpertProfileImageRef;
    final static int Gallery_Pick =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_expert_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("Expert Account");

        loadingBar= new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser expert = FirebaseAuth.getInstance().getCurrentUser();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Expert").child(expert.getUid());

        ExpertProfileImageRef = FirebaseStorage.getInstance().getReference().child("Expert Profile Images").child(expert.getUid());;

        ExpertFullName = (EditText) findViewById(R.id.add_ExpertFullName);
        ExpertEmail = (EditText) findViewById(R.id.add_ExpertEmail);
        ExpertMobile = (EditText) findViewById(R.id.add_ExpertMobile);
        ExpertQualification =(EditText) findViewById(R.id.add_ExpertEducation);
        ExpertCity=(EditText) findViewById(R.id.add_ExpertCity);
        ExpertProvince=(EditText) findViewById(R.id.add_ExpertProvince);
        ExpertCountry=(EditText) findViewById(R.id.add_ExpertCountry);
        ExpertExperties=(EditText) findViewById(R.id.add_ExpertExperties);


        Button createExpert = findViewById(R.id.registerExpert);
        createExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateExpert();
            }
        });

        ExpertProfile = (CircleImageView) findViewById(R.id.add_expert_profile);
        ExpertProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                // startActivityForResult(galleryIntent, Gallery_Pick);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"),Gallery_Pick);
            }
        });

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("expertprofileimage"))
                    {
                        String image = dataSnapshot.child("expertprofileimage").getValue().toString();
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(ExpertProfile);
                    }
                    else
                    {
                        Toast.makeText(RegisterExpertProfile.this, "Please select profile image first.", Toast.LENGTH_SHORT).show();
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

                final StorageReference filePath = ExpertProfileImageRef.child(currentExpertID + ".jpg");

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
                            Toast.makeText(RegisterExpertProfile.this, "Profile Image stored successfully to Firebase storage...", Toast.LENGTH_SHORT).show();
                            final String downloadUrl = downUri.toString();
                            UsersRef.child("expertprofileimage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {

                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent selfIntent = new Intent(RegisterExpertProfile.this,RegisterExpertProfile.class);
                                                startActivity(selfIntent);

                                                Toast.makeText(RegisterExpertProfile.this, "Profile Image stored to Firebase Database Successfully...", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            } else {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(RegisterExpertProfile.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
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
    public void CreateExpert() {

        final String Expert_Fullname = ExpertFullName.getText().toString().trim();
        final String Expert_Email = ExpertEmail.getText().toString().trim();
        final String Expert_MobileNo = ExpertMobile.getText().toString().trim();
        final String Expert_Qualification = ExpertQualification.getText().toString().trim();
        final String Expert_City= ExpertCity.getText().toString().trim();
        final String Expert_Province = ExpertProvince.getText().toString().trim();
        final String Expert_Country = ExpertCountry.getText().toString().trim();
        final String Expert_Experties = ExpertExperties.getText().toString().trim();


        if (TextUtils.isEmpty(Expert_Fullname)) {
            Toast.makeText(RegisterExpertProfile.this, "Please write your full name...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Expert_Email)) {
            Toast.makeText(RegisterExpertProfile.this, "Please write your email...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Expert_MobileNo)) {
            Toast.makeText(RegisterExpertProfile.this, "Please write your mobile...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Expert_Qualification)) {
            Toast.makeText(RegisterExpertProfile.this, "Please write your qualification...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Expert_City)) {
            Toast.makeText(RegisterExpertProfile.this, "Please write your city...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Expert_Province)) {
            Toast.makeText(RegisterExpertProfile.this, "Please write your province...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Expert_Country)) {
            Toast.makeText(RegisterExpertProfile.this, "Please write your country...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Expert_Experties)) {
            Toast.makeText(RegisterExpertProfile.this, "Please write your experise...", Toast.LENGTH_SHORT).show();
        }

        else
        {
            loadingBar.setTitle("Authenticating");
            loadingBar.setMessage("Please Wait, while we are your new account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap expertMap = new HashMap();

            expertMap.put("expert_FullName",Expert_Fullname);
            expertMap.put("expert_Email",Expert_Email);
            expertMap.put("expert_MobileNo",Expert_MobileNo);
            expertMap.put("expert_Qualification",Expert_Qualification);
            expertMap.put("expert_City",Expert_City);
            expertMap.put("expert_Province",Expert_Province);
            expertMap.put("expert_Country",Expert_Country);
            expertMap.put("expert_Experties",Expert_Experties);

            loadingBar.dismiss();
            loadingBar.setMessage("Creating Your Account");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);


            UsersRef.updateChildren(expertMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(RegisterExpertProfile.this, "Account Created Succesfully.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        startActivity(new Intent(getApplicationContext(), ExpertDashboard.class));
                        finish();
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(RegisterExpertProfile.this, "Error: " + message, Toast.LENGTH_SHORT).show();
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
