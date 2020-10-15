package com.example.agrisol.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agrisol.R;
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

public class UserHome extends Fragment {


ImageView imageView;
TextView choose,cam;
TextView resultText;
FirebaseVisionImageLabeler labeler;
    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imageView = getView().findViewById( R.id.imageView );
        choose = getView().findViewById( R.id.capture );
        resultText = getView().findViewById( R.id.textView );
        cam = getView().findViewById( R.id.camera );
        //noinspection deprecation
        FirebaseAutoMLLocalModel localModel = new FirebaseAutoMLLocalModel.Builder()
                .setAssetFilePath( "model/manifest.json" )
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

            }
        } );


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if(requestCode==121){
            imageView.setImageURI( data.getData() );

            FirebaseVisionImage image;
            try {
                image = FirebaseVisionImage.fromFilePath(getContext(), data.getData());

                labeler.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionImageLabel> labels) {
                                // Task completed successfully
                                // ...
                                for (FirebaseVisionImageLabel label: labels) {
                                    String text = label.getText();
                                    float confidence =  label.getConfidence();
                                    resultText.append( text+" = "+""+confidence+"\n" );
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
