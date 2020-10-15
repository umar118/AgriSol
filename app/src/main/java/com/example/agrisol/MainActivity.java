package com.example.agrisol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.FirebaseMLException;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.automl.FirebaseAutoMLLocalModel;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.label.FirebaseVisionOnDeviceAutoMLImageLabelerOptions;

import java.io.IOException;
import java.util.List;


public class MainActivity extends AppCompatActivity{

  private   ImageView imageView;
  private   TextView resultTV;
  private    Button choose;
  private FirebaseVisionImageLabeler labeler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById( R.id.resultShow );
        resultTV = findViewById( R.id.resultText );
        choose = findViewById( R.id.Capture );
        choose.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(  );
                i.setType( "image/*" );
                i.setAction( Intent.ACTION_GET_CONTENT );
                startActivityForResult( Intent.createChooser( i,"Choose An Image" ),121 );
            }
        } );



        //noinspection deprecation
        FirebaseAutoMLLocalModel localModel = new FirebaseAutoMLLocalModel.Builder()
                .setAssetFilePath("model/manifest.json")
                .build();
        try {
            //noinspection deprecation
            FirebaseVisionOnDeviceAutoMLImageLabelerOptions options = new FirebaseVisionOnDeviceAutoMLImageLabelerOptions.Builder( localModel )
                    .setConfidenceThreshold( 0.05f )  // Evaluate your model in the Firebase console
                    // to determine an appropriate value.
                    .build();
            //noinspection deprecation
            labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);
        } catch (FirebaseMLException e) {
            // ...
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if(requestCode==121){
            imageView.setImageURI( data.getData() );

            FirebaseVisionImage image;
            try {
                image = FirebaseVisionImage.fromFilePath(getApplicationContext(), data.getData());

                labeler.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                                // Task completed successfully
                                // ...
                                for (FirebaseVisionImageLabel label: labels) {
                                    String text = label.getText();
                                    float confidence =  label.getConfidence();
                                    resultTV.append( text+" = "+""+confidence+"\n" );
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                // ...
                            }
                        });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
