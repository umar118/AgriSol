package com.example.agrisol.Login;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.annotations.Nullable;

public class DeleteAccountDailogFragment extends DialogFragment {


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Account");
        builder.setMessage("Are you sure do you want to delete account permanently?");
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),"You are clicked OK",Toast.LENGTH_SHORT).show();

                if(getActivity() !=null){
                    getActivity().finish();
                }
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getActivity(),"You are clicked CANCEL",Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog =builder.create();
        return alertDialog;
    }
}
