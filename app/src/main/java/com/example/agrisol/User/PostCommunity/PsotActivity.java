package com.example.agrisol.User.PostCommunity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.agrisol.R;
import com.example.agrisol.User.UserDashboard;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PsotActivity extends AppCompatActivity {


    private ProgressDialog loadingBar;
    private ImageButton imageBtn;
    private static final int GALLERY_REQUEST_CODE = 2;

    private Uri uri;
    private String Description,Title;
    private EditText textTitle;
    private EditText textDesc;

    private Button UpdatePostButton;

    private StorageReference storage;

    private FirebaseAuth mAuth;
    private DatabaseReference UserRef, PostsRef;

    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadUrl, current_user_id;

    private long countPosts = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_psot );
        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayShowTitleEnabled( false );
        TextView mTitle = toolbar.findViewById( R.id.toolbar_title );
        mTitle.setText( "Add Post" );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setDisplayShowHomeEnabled( true );

        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity( new Intent( getApplicationContext(), UserDashboard.class ) );
                finish();
            }
        } );





            UpdatePostButton = (Button) findViewById( R.id.postBtn );
            textDesc = (EditText) findViewById( R.id.textDesc );
            textTitle = (EditText) findViewById( R.id.textTitle );
            storage = FirebaseStorage.getInstance().getReference();

            PostsRef = FirebaseDatabase.getInstance().getReference().child( "Post" );
            UserRef = FirebaseDatabase.getInstance().getReference().child( "Users" ).child( "User" );

            mAuth = FirebaseAuth.getInstance();
            current_user_id = mAuth.getCurrentUser().getUid();

            loadingBar = new ProgressDialog( this );


            imageBtn = (ImageButton) findViewById( R.id.imageBtn );



            imageBtn.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenGallery();
                }
            } );


            UpdatePostButton.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ValidatePostInfo();
                }
            } );


    }

    private void ValidatePostInfo()
    {
        Title = textTitle.getText().toString();
        Description = textDesc.getText().toString();


     if(uri == null)
        {
            Toast.makeText(this, "Please select post image...", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(Title))
        {
                textTitle.setError( "Please Enter Any Title" );
        }
        else if(TextUtils.isEmpty(Description))
        {
               textDesc.setError( "Please write something about selected image" );
        }
        else
        {
            loadingBar.setTitle("Add New Post");
            loadingBar.setMessage("Please wait, while we are updating your new post...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            StoringImageToFirebaseStorage();
        }
    }

    private void StoringImageToFirebaseStorage()
    {
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calFordDate.getTime());


        postRandomName = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = storage.child("Post Images").child(uri.getLastPathSegment() + postRandomName + ".jpg");

        filePath.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
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
                    Toast.makeText(getApplicationContext(), "Profile Image stored successfully to Firebase storage...", Toast.LENGTH_SHORT).show();
                    downloadUrl = downUri.toString();
                    UserRef.child("userprofileimage").setValue(downloadUrl)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {

                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {


                                        Toast.makeText(getApplicationContext(), "image uploaded successfully to Storage...", Toast.LENGTH_SHORT).show();

                                        SavingPostInformationToDatabase();


                                    }
                                    else
                                    {
                                        String message = task.getException().getMessage();
                                        Toast.makeText(getApplicationContext(), "Error occured: " + message, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }

        });


    }


    private void SavingPostInformationToDatabase()
    {

        PostsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    countPosts = dataSnapshot.getChildrenCount();
                }
                else
                {
                    countPosts = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        UserRef.child(current_user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    String userFullName = dataSnapshot.child("user_FullName").getValue().toString();
                    String userProfileImage = dataSnapshot.child("userprofileimage").getValue().toString();
                     Post post =new Post(  );
                    HashMap postsMap = new HashMap();
                    postsMap.put("uid", current_user_id);
                    postsMap.put("date", saveCurrentDate);
                    postsMap.put("time", saveCurrentTime);
                    postsMap.put( "title",Title );
                    postsMap.put("description", Description);
                    postsMap.put("postimage", downloadUrl);
                    postsMap.put("profileimage", userProfileImage);
                    postsMap.put("fullname", userFullName);
                    postsMap.put("counter",countPosts);
                    postsMap.put("latitude",post.getLatitude());
                    postsMap.put("longitude",post.getLongitude());
                    PostsRef.child(current_user_id + postRandomName).updateChildren(postsMap)
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        startActivity( new Intent( getApplicationContext(),PsotActivity.class ) );
                                        finish();
                                        Toast.makeText(getApplicationContext(), "New Post is updated successfully.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "Error Occured while updating your post.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null){
            uri = data.getData();
            imageBtn.setImageURI(uri);
        }
    }
}
