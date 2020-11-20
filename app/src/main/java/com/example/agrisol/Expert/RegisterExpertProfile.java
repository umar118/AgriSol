package com.example.agrisol.Expert;

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

import com.example.agrisol.Login;
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
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterExpertProfile extends AppCompatActivity {
    private EditText ExpertFullName, ExpertEmail,ExpertCity, ExpertProvince,ExpertCountry,ExpertQualification,ExpertMobile,ExpertExperties;
    private FirebaseAuth mAuth;
    private CircleImageView ExpertProfile;
    private DatabaseReference ExpertRef;
    private ProgressDialog loadingBar;
    String currentExpertID;
    private StorageReference ExpertProfileImageRef;
    final static int Gallery_Pick =1;
    private static final Pattern TEXT_PATTERN =Pattern.compile( "([ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz])*");

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
        ExpertRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Expert").child(expert.getUid());

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

        ExpertRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("expert_Profile"))
                    {
                        String image = dataSnapshot.child("expert_Profile").getValue().toString();
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
                            ExpertRef.child("expert_Profile").setValue(downloadUrl)
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

        final String Expert_FullName = ExpertFullName.getText().toString().trim();
        final String Expert_Email = ExpertEmail.getText().toString().trim();
        final String Expert_MobileNo = ExpertMobile.getText().toString().trim();
        final String Expert_Qualification = ExpertQualification.getText().toString().trim();
        final String Expert_City= ExpertCity.getText().toString().trim();
        final String Expert_Province = ExpertProvince.getText().toString().trim();
        final String Expert_Country = ExpertCountry.getText().toString().trim();
        final String Expert_Experties = ExpertExperties.getText().toString().trim();


        if(ExpertProfile !=null){
            Toast.makeText( getApplicationContext(),"Please Select Image",Toast.LENGTH_SHORT ).show();
        }
        if (TextUtils.isEmpty(Expert_FullName)) {
           ExpertFullName.setError( "Enter Name" );
        }
        else if (!TEXT_PATTERN.matcher( Expert_FullName ).matches()){
            ExpertFullName.setError( "Enter Only Text" );
        }
        else if (TextUtils.isEmpty(Expert_Email)) {
            ExpertEmail.setError( "Enter Registered Email Address" );
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher( Expert_Email ).matches()){
            ExpertEmail.setError( "Please Enter Valid Email" );
        }
        else if (TextUtils.isEmpty(Expert_MobileNo)) {
           ExpertMobile.setError( "Enter Mobile Number" );
        }
        else if (!Patterns.PHONE.matcher( Expert_MobileNo ).matches()){
            ExpertMobile.setError( "Enter Valid Mobile Number" );
        }
        else if (!Patterns.PHONE.matcher( Expert_MobileNo ).matches()){

            ExpertMobile.setError( "Enter Valid Mobile Number" );
        }
        else if (TextUtils.isEmpty(Expert_Qualification)) {
           ExpertQualification.setError( "Enter Qualification" );
        }
        else if (!TEXT_PATTERN.matcher( Expert_Qualification ).matches()){
            ExpertQualification.setError( "Enter Only Text" );
        }
        else if (TextUtils.isEmpty(Expert_City)) {
           ExpertCity.setError( "Enter City Name" );
        }
        else if (!TEXT_PATTERN.matcher( Expert_City ).matches()){
            ExpertCity.setError( "Enter Only Text" );
        }
        else if (TextUtils.isEmpty(Expert_Province)) {
           ExpertProvince.setError( "Enter Province" );
        }
        else if (!TEXT_PATTERN.matcher( Expert_Province ).matches()){
            ExpertProvince.setError( "Enter Only Text" );
        }
        else if (TextUtils.isEmpty(Expert_Country)) {
            ExpertCountry.setError( "Enter Country" );
        }
        else if (!TEXT_PATTERN.matcher( Expert_Country ).matches()){
            ExpertCountry.setError( "Enter Only Text" );
        }
        else if (TextUtils.isEmpty(Expert_Experties)) {
           ExpertExperties.setError( "Enter Expertise" );
        }
        else if (!TEXT_PATTERN.matcher( Expert_Experties ).matches()){
            ExpertExperties.setError( "Enter Only Text" );
        }

        else
        {
            loadingBar.setTitle("Authenticating");
            loadingBar.setMessage("Please Wait, while we are your new account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);


            HashMap expertMap = new HashMap();
            expertMap.put("expert_FullName",Expert_FullName);
            expertMap.put("expert_Email",Expert_Email);
            expertMap.put("expert_MobileNo",Expert_MobileNo);
            expertMap.put("expert_Qualification",Expert_Qualification);
            expertMap.put("expert_City",Expert_City);
            expertMap.put("expert_Province",Expert_Province);
            expertMap.put("expert_Country",Expert_Country);
            expertMap.put("expert_Expertise",Expert_Experties);


            loadingBar.dismiss();
            loadingBar.setMessage("Creating Your Account");
            loadingBar.show();

           ExpertRef.updateChildren(expertMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(RegisterExpertProfile.this, "Account Created Successfully.", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        SendToExpertDashboard();
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

    private void  SendToExpertDashboard(){
        startActivity( new Intent( getApplicationContext(),ExpertDashboard.class ) );
        finish();
    }


}
