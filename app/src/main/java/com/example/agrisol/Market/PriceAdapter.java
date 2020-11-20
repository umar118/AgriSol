package com.example.agrisol.Market;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrisol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.ViewHolder> implements Filterable {
    private static final Pattern TEXT_PATTERN =Pattern.compile( "([ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz])*");
    private static final Pattern NUMBER_PATTERN =Pattern.compile( "([0123456789])*");
    Context context;
    ArrayList<Market> model;
    ArrayList<Market> modelFilter;
    Dialog dialog;
    HashMap<String, Object> crop_map = new HashMap<>();

    public PriceAdapter(Context context, ArrayList<Market> model) {
        this.context = context;
        this.model = model;
        modelFilter = new ArrayList<>( model );
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.market_list, parent, false );
        final ViewHolder viewHolder = new ViewHolder( layoutView );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.CropName.setText( model.get( position ).getName() );
        holder.CropPrice.setText( model.get( position ).getPrice() );
        holder.CropDistrict.setText( model.get( position ).getDistrict() );
        holder.CropDate.setText( model.get( position ).getDate() );
        holder.edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog( context );
                dialog.setContentView( R.layout.update_price_list );
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                final EditText name = (EditText) dialog.findViewById( R.id.Update_CropName );
                final EditText price = (EditText) dialog.findViewById( R.id.Update_CropPrice );
                final EditText dis = (EditText) dialog.findViewById( R.id.Update_CropDistrict );
                final EditText date = (EditText) dialog.findViewById( R.id.Update_current_date );
                CardView update = (CardView) dialog.findViewById( R.id.update );

                if (!TEXT_PATTERN.matcher( name.getText() ).matches()){
                    name.setError( "Enter Only Text" );
                }
                else if (!TEXT_PATTERN.matcher( dis.getText() ).matches()){
                    dis.setError( "Enter Only Text" );
                }
                else if (!NUMBER_PATTERN.matcher( price.getText() ).matches()){
                    price.setError( "Enter Only Number" );
                }

                name.setText( model.get( holder.getAdapterPosition() ).getName() );
                price.setText( model.get( holder.getAdapterPosition() ).getPrice() );
                dis.setText( model.get( holder.getAdapterPosition() ).getDistrict() );
                date.setText( model.get( holder.getAdapterPosition() ).getDate() );

                update.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        {
                            final DatabaseReference PriceRef = FirebaseDatabase.getInstance().getReference().child( "Market_Rate" ).child( model.get( position ).getUid() );
                            crop_map.put( "name", name.getText().toString() );
                            crop_map.put( "price", price.getText().toString() );
                            crop_map.put( "district", dis.getText().toString() );
                            crop_map.put( "date", date.getText().toString() );

                            PriceRef.updateChildren( crop_map ).addOnCompleteListener( new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        dialog.dismiss();

                                        Toast.makeText( context, "Data Saved!", Toast.LENGTH_SHORT ).show();
                                    } else {
                                        String message = task.getException().getMessage();
                                    }
                                }
                            } );
                        }
                    }
                } );

                dialog.show();
            }

        } );

        holder.del.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder( holder.CropName.getContext() );
                builder.setTitle( "Delete Panel" );
                builder.setMessage( "Delete...?" );
                builder.setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                                final DatabaseReference PriceRef =
                                FirebaseDatabase.getInstance().getReference().child( "Market_Rate" ).child( model.get( position ).getUid() );
                                PriceRef.removeValue(  );
                    }
                } );
                builder.setNegativeButton( "No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                } );
                builder.show();

            }
        } );

    }

    @Override
    public int getItemCount() {
        return model.size();
    }


    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Market> filterList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filterList.addAll( modelFilter );
            } else {
                String pattrn = constraint.toString().toLowerCase().trim();
                for (Market item : modelFilter) {
                    if (item.getName().toLowerCase().contains( pattrn )) {
                        filterList.add( item );
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            model.clear();
            model.addAll( (ArrayList) results.values );
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout market_list;
        ImageView edit, del;
        TextView CropName, CropPrice, CropDistrict, CropDate;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );
            market_list = (LinearLayout) itemView.findViewById( R.id.market_list_id );

            CropName = (TextView) itemView.findViewById( R.id.cropName );
            CropPrice = (TextView) itemView.findViewById( R.id.cropPrice );
            CropDistrict = (TextView) itemView.findViewById( R.id.cropDistrict );
            CropDate = (TextView) itemView.findViewById( R.id.cropDate );
            edit = (ImageView) itemView.findViewById( R.id.edit );
            del = (ImageView) itemView.findViewById( R.id.delete );

        }
    }

}
