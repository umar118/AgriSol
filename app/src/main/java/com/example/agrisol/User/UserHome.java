package com.example.agrisol.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.agrisol.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.io.IOException;
import java.util.List;

public class UserHome extends Fragment {


ImageView imageView;
TextView choose,cam,translate,mSourceLang;
TextView resultText;
private String sourceText;
FirebaseVisionImageLabeler labeler;
    static final int CAMERA_REQUEST = 123;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imageView = getView().findViewById( R.id.imageView );
        mSourceLang = getView().findViewById(R.id.soureText);
        choose = getView().findViewById( R.id.capture );
        resultText = getView().findViewById( R.id.textView );
        cam = getView().findViewById( R.id.camera );
        translate = getView().findViewById( R.id.translateData );
        translate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              identifyLanguage();
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
                startActivityForResult( intent,CAMERA_REQUEST);
            }
        } );


    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        FirebaseVisionImage image;

        if(requestCode==121){
            imageView.setImageURI( data.getData() );

            try {
                image = FirebaseVisionImage.fromFilePath(getContext(), data.getData());

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
                                Toast.makeText( getContext(),"Unknown",Toast.LENGTH_SHORT ).show();
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
                image = FirebaseVisionImage.fromFilePath(getContext(), data.getData());

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
                                Toast.makeText( getContext(),"Unknown",Toast.LENGTH_SHORT ).show();
                            }
                        });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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

    private void identifyLanguage() {
        sourceText = resultText.getText().toString();

        FirebaseLanguageIdentification identifier = FirebaseNaturalLanguage.getInstance()
                .getLanguageIdentification();

       // mSourceLang.setText("Detecting..");
        identifier.identifyLanguage(sourceText).addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                if (s.equals("und")){
                    Toast.makeText(getContext(),"Language Not Identified",Toast.LENGTH_SHORT).show();

                }
                else {
                    getLanguageCode(s);
                }
            }
        });
    }
    private void getLanguageCode(String language) {
        int langCode;
        switch (language){
            case "en":
                langCode = FirebaseTranslateLanguage.EN;
                mSourceLang.setText("English");

                break;
            default:
                langCode = 0;
        }

        translateText(langCode);
    }

}
