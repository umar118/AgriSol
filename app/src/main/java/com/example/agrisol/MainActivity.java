package com.example.agrisol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.annotations.Nullable;
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


public class MainActivity extends AppCompatActivity{

    private static final String TAG = null;
    private   ImageView imageView;
  private   TextView resultTV;
  private    Button camera,Ref;
  private FirebaseVisionImageLabeler labeler;


    private TextView mSourceLang;
    private EditText mSourcetext;
    private Button mTranslateBtn;
    private TextView mTranslatedText;
    private String sourceText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSourceLang = findViewById(R.id.soureText);
        mTranslateBtn = findViewById(R.id.translate);
        mTranslatedText = findViewById(R.id.translate_text);
        mTranslateBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                identifyLanguage();
            }
        } );

        imageView = findViewById( R.id.resultShow );
        resultTV = findViewById( R.id.resultText );
        camera = findViewById( R.id.Capture );
        Ref = findViewById( R.id.refresh );

        Ref.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Refresh();
            }
        } );

        camera.setOnClickListener( new View.OnClickListener() {
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
                    .setConfidenceThreshold( 0.5f )  // Evaluate your model in the Firebase console
                    // to determine an appropriate value.
                    .build();
            //noinspection deprecation
            labeler = FirebaseVision.getInstance().getOnDeviceAutoMLImageLabeler(options);
        } catch (FirebaseMLException e) {
            // ...
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if(requestCode==121){
            imageView.setImageURI( data.getData() );

            final FirebaseVisionImage image;
            try {
                image = FirebaseVisionImage.fromFilePath(getApplicationContext(), data.getData());

                labeler.processImage(image)
                        .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionImageLabel>>() {
                            @Override
                            public void onSuccess(List<FirebaseVisionImageLabel> labels) {

                                    for (FirebaseVisionImageLabel label : labels) {
                                        String text = label.getText();
                                        float confidence = label.getConfidence();
                                        resultTV.append( text + "\n"+" = "+" "+ confidence + "\n" );

                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                resultTV.append( e.getMessage() );

                            }
                        });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    private void identifyLanguage() {
        sourceText = resultTV.getText().toString();

        FirebaseLanguageIdentification identifier = FirebaseNaturalLanguage.getInstance()
                .getLanguageIdentification();

        mSourceLang.setText("Detecting..");
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

    private void getLanguageCode(String language) {
        int langCode;
        switch (language){
            case "hi":
                langCode = FirebaseTranslateLanguage.HI;
                mSourceLang.setText("Hindi");
                break;
            case "ar":
                langCode = FirebaseTranslateLanguage.AR;
                mSourceLang.setText("Arabic");

                break;
            case "en":
                langCode = FirebaseTranslateLanguage.EN;
                mSourceLang.setText("In Urdu");

                break;
            default:
                langCode = 0;
        }

        translateText(langCode);
    }

    private void translateText(int langCode) {
        mTranslatedText.setText("Translating..");
        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                //from language
                .setSourceLanguage(langCode)
                // to language
                .setTargetLanguage(FirebaseTranslateLanguage.UR)
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
                        mTranslatedText.setText(s);
                    }
                });
            }
        });
    }


    private  void  Refresh(){
        startActivity( new Intent( getApplicationContext(),MainActivity.class) );
        finish();
    }

}
