package com.example.agrisol;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.languageid.FirebaseLanguageIdentification;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommonHome extends AppCompatActivity {

    ImageView imageView;
    Button choose,cam,translateUrdu,Refresh;
    TextView resultText;
    private String sourceText,sourceText2;
    FirebaseVisionImageLabeler labeler;
    static final int CAMERA_REQUEST = 123;

    DatabaseReference Ref;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_common_home );
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("AgriSolutions");
        final CircleImageView login =findViewById( R.id.toolbar_login );
        login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getApplicationContext(), Login.class ) );
                finish();
            }
        } );

        Refresh = findViewById( R.id.refresh );
        imageView = findViewById( R.id.ImageResult );
        choose = findViewById( R.id.gallery );
        resultText = findViewById( R.id.TextResult );
        cam = findViewById( R.id.cameraImage );
        translateUrdu = findViewById( R.id.translateUrdu );
        translateUrdu.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                identifyLanguage();
            }
        } );

        Ref = FirebaseDatabase.getInstance().getReference().child( "Users" ).child( "User" );

        Ref.addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild( "userprofileimage" )){
                    String image = snapshot.child("userprofileimage").getValue().toString();
                    Picasso.get().load(image).placeholder(R.drawable.profile).into(login);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText( getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT ).show();
            }
        } );


        //noinspection deprecation
        FirebaseAutoMLLocalModel localModel = new FirebaseAutoMLLocalModel.Builder()
                .setAssetFilePath( "model/manifest.json" )
                .build();


        try {
            //noinspection deprecation
            FirebaseVisionOnDeviceAutoMLImageLabelerOptions options = new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder( localModel )
                    .setConfidenceThreshold( 0.5f )  // Evaluate your model in the Firebase console
                    // to determine an appropriate value.
                    .build();
            //noinspection deprecation
            labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);
        } catch (FirebaseMLException e) {
            // ...
        }


        choose.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(  );
                i.setType( "image/*" );
                i.setAction( Intent.ACTION_GET_CONTENT );
                startActivityForResult( Intent.createChooser( i,"Choose An Image" ),121 );


            }
        } );

        cam.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  =new Intent( MediaStore.ACTION_IMAGE_CAPTURE );

                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED){
                                requestPermission();

                }
                if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE ) != PackageManager.PERMISSION_GRANTED){
                    requestPermission2();
                }
                else {
                    startActivityForResult( intent,CAMERA_REQUEST);
                }
            }
        } );


        Refresh.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getApplicationContext(),CommonHome.class ) );
                finish();
            }
        } );

    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(CommonHome.this,new String[]{Manifest.permission.CAMERA},CAMERA_REQUEST );
    }
    private void requestPermission2(){
        ActivityCompat.requestPermissions(CommonHome.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},CAMERA_REQUEST );
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        FirebaseVisionImage image;

        if(requestCode==121){
            imageView.setImageURI( data.getData() );

            try {
                image = FirebaseVisionImage.fromFilePath(getApplicationContext(), data.getData());

                labeler.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                                for (FirebaseVisionImageLabel label : labels) {
                                    String text = label.getText();
                                    float confidence = label.getConfidence();
                                    resultText.append( text + "\n"+" = "+" "+ confidence + "\n" );
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText( getApplicationContext(),"Unknown",Toast.LENGTH_SHORT ).show();
                            }
                        });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(requestCode ==CAMERA_REQUEST){
            Bitmap photo = (Bitmap) data.getExtras().get( "data" );
            imageView.setImageBitmap( photo );
            try {
                image = FirebaseVisionImage.fromFilePath(getApplicationContext(), data.getData());

                labeler.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                                for (FirebaseVisionImageLabel label : labels) {
                                    String text = label.getText();
                                    float confidence = label.getConfidence();
                                    resultText.append( text + " = " + "" + confidence + "\n" );
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText( getApplicationContext(),"Unknown",Toast.LENGTH_SHORT ).show();
                            }
                        });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void identifyLanguage() {
        sourceText = resultText.getText().toString();

        FirebaseLanguageIdentification identifier = FirebaseNaturalLanguage.getInstance()
                .getLanguageIdentification();

        // mSourceLang.setText("Detecting..");
        identifier.identifyLanguage(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s.equals("und")){
                    Toast.makeText(getApplicationContext(),"Language Not Identified",Toast.LENGTH_SHORT).show();

                }
                else {
                    getLanguageCode(s);
                }
            }
        });
    }


    private void translateText(int langCode) {
        resultText.setText("Translating..");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                //from language
                .setSourceLanguage(langCode)
                // to language
                .setTargetLanguage( FirebaseTranslateLanguage.UR)
                .build();

        final FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance()
                .getTranslator(options);

        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder()
                .build();
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                translator.translate(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        resultText.setText(s);
                    }
                });
            }
        });
    }


    private void getLanguageCode(String language) {
        int langCode;
        switch (language){
            case "en":
                langCode = FirebaseTranslateLanguage.EN;
                // mSourceLang.setText("English");
            default:
                langCode = 0;
        }

        translateText(langCode);
    }

}
