package com.example.agrisol.Admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.agrisol.Market.Market;
import com.example.agrisol.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.regex.Pattern;

public class AddMarketList extends AppCompatActivity {

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    private EditText CropName, CropPrice, CropDistrict ;
    private TextView CurrentDate;
    private DatabaseReference PriceRef;
    private ProgressDialog loadingBar;
    private CardView AddCrop;

    private static final Pattern TEXT_PATTERN =Pattern.compile( "([ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz])*");
    private static final Pattern NUMBER_PATTERN =Pattern.compile( "([0123456789])*");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.add_market_list );

        CropName = findViewById( R.id.Edit_CropName );
        CropPrice = findViewById( R.id.Edit_price );
        CropDistrict = findViewById( R.id.Edit_district );
        CurrentDate = findViewById( R.id.Edit_current_date );
        PriceRef = FirebaseDatabase.getInstance().getReference( "Market_Rate" );

        AddCrop = findViewById( R.id.Add_List );
        AddCrop.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddList();
            }
        } );


        Toolbar toolbar = findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        getSupportActionBar().setDisplayShowTitleEnabled( false );
        TextView mTitle = toolbar.findViewById( R.id.toolbar_title );
        mTitle.setText( "Add Market Item" );
        getSupportActionBar().setDisplayHomeAsUpEnabled( true );
        getSupportActionBar().setDisplayShowHomeEnabled( true );
        toolbar.setNavigationOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getApplicationContext(), AdminDashboard.class ) );
                finish();
            }
        } );

        loadingBar = new ProgressDialog( this );

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);




    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }
    @Override
    protected Dialog onCreateDialog(int id) {

        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        CurrentDate.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }




    private void AddList() {

        final String Crop_Name = CropName.getText().toString().trim();
        final String Crop_Price = CropPrice.getText().toString().trim();
        final String Crop_District = CropDistrict.getText().toString().trim();
        final String Crop_Date = CurrentDate.getText().toString().trim();

        if (TextUtils.isEmpty( Crop_Name )) {
           CropName.setError( "Please Enter Crop's Name" );
        }
        else if (!TEXT_PATTERN.matcher( Crop_Name ).matches()){
           CropName.setError( "Enter Only Text" );
        }
        else if (TextUtils.isEmpty( Crop_Price )) {
            CropPrice.setError( "Please Enter Price" );
        }
        else if (!NUMBER_PATTERN.matcher( Crop_Price ).matches()){
            CropPrice.setError( "Enter Only Number" );
        }
        else if (TextUtils.isEmpty( Crop_District )) {
            CropDistrict.setError( "Please Enter District" );
        }
        else if (!TEXT_PATTERN.matcher( Crop_District ).matches()){
            CropDistrict.setError( "Enter Only Text" );
        }
        else if (TextUtils.isEmpty( Crop_Date )) {
            Toast.makeText( AddMarketList.this, "Please Write Date", Toast.LENGTH_SHORT ).show();
        } else {
            loadingBar.setTitle( "Saving Information" );
            loadingBar.setMessage( "wait for adding data..." );
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside( true );

            String id = PriceRef.push().getKey();
            Log.d( "IdddddddddD", "AddList: "+id );

            Market market = new Market( Crop_Name, Crop_Price, Crop_District, Crop_Date, id );
            PriceRef.child( id ).setValue( market );
            Toast.makeText( this, "Data Saved !", Toast.LENGTH_SHORT ).show();
            loadingBar.dismiss();
                Clear();
                startActivity(  new Intent( getApplicationContext(),AdminDashboard.class ) );
                finish();

        }
    }

    public void Clear() {
        CropName.getText().clear();
        CropPrice.getText().clear();
        CropDistrict.getText().clear();
    }

}
