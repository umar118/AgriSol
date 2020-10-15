package com.example.agrisol.Market;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrisol.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.xml.transform.Result;

public class PriceAdapter extends RecyclerView.Adapter<PriceAdapter.ViewHolder> implements Filterable {

  Context context;
  ArrayList<Market> model;
  ArrayList<Market> modelFilter;
  Dialog dialog;

    public PriceAdapter(Context context, ArrayList<Market> model) {
        this.context = context;
        this.model = model;
        modelFilter =new ArrayList<>( model );
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_list, parent, false);
        final ViewHolder viewHolder =new ViewHolder(layoutView);

      /*  dialog =new Dialog( context );
        dialog.setContentView( R.layout.update_price_list );
        dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );



        viewHolder.market_list.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText name = (EditText) dialog.findViewById( R.id.Update_CropName );
                final EditText price = (EditText) dialog.findViewById( R.id.Update_CropPrice );
                final EditText dis = (EditText) dialog.findViewById( R.id.Update_CropDistrict );
                final EditText date = (EditText) dialog.findViewById( R.id.Update_current_date );
                CardView update = (CardView) dialog.findViewById( R.id.update );

                name.setText( model.get( viewHolder.getAdapterPosition() ).getName() );
                price.setText( model.get( viewHolder.getAdapterPosition() ).getPrice() );
                dis.setText( model.get( viewHolder.getAdapterPosition() ).getDistrict() );
                date.setText( model.get( viewHolder.getAdapterPosition() ).getDate() );
                update.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        HashMap<String ,Object> crop_map = new HashMap<>();

                        crop_map.put("CropName", name.getText().toString());
                        crop_map.put("CropPrice", price.getText().toString());
                        crop_map.put("CropDistrict", dis.getText().toString());
                        crop_map.put("Current_Date",date.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child( "Market Rate" );



                    }
                } );
                Toast.makeText( context,""+String.valueOf( viewHolder.getAdapterPosition() ),Toast.LENGTH_SHORT ).show();
                dialog.show();
            }
        } );
*/
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.CropName.setText(model.get(position).getName());
        holder.CropPrice.setText(model.get(position).getPrice());
        holder.CropDistrict.setText(model.get(position).getDistrict());
        holder.CropDate.setText(model.get(position).getDate());
        holder.edit.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog =new Dialog( context );
                dialog.setContentView( R.layout.update_price_list );
                dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );
                final EditText name = (EditText) dialog.findViewById( R.id.Update_CropName );
                final EditText price = (EditText) dialog.findViewById( R.id.Update_CropPrice );
                final EditText dis = (EditText) dialog.findViewById( R.id.Update_CropDistrict );
                final EditText date = (EditText) dialog.findViewById( R.id.Update_current_date );
                CardView update = (CardView) dialog.findViewById( R.id.update );

                name.setText( model.get( holder.getAdapterPosition() ).getName() );
                price.setText( model.get( holder.getAdapterPosition() ).getPrice() );
                dis.setText( model.get( holder.getAdapterPosition() ).getDistrict() );
                date.setText( model.get( holder.getAdapterPosition() ).getDate() );

                update.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        FirebaseDatabase fb = FirebaseDatabase.getInstance();

                    }
                } );


                dialog.show();


            }

        } );
    }

    public  void  updateValue(String text, Result r, final View v){

        final  View view =v;
        final  Result result = r;

        FirebaseDatabase fb = FirebaseDatabase.getInstance();
        final DatabaseReference ref = fb.getReference().child( "Market Rate" );

        ref.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    ref.child( "CropPrice" ).setValue( result );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );

    }


    @Override
    public int getItemCount() {
        return model.size();
    }


    private Filter filter =new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Market> filterList =new ArrayList<>( );
            if(constraint==null|| constraint.length()==0){
                filterList.addAll(modelFilter);
            }
            else {
                String pattrn = constraint.toString().toLowerCase().trim();
                for (Market item : modelFilter) {
                    if (item.getName().toLowerCase().contains( pattrn )) {
                        filterList.add( item );
                    }
                }
            }
            FilterResults filterResults=new FilterResults();
            filterResults.values=filterList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            model.clear();
            model.addAll( (ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

    @Override
    public Filter getFilter() {
        return filter;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout market_list;
        ImageView edit,del;
        TextView CropName,CropPrice,CropDistrict,CropDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            market_list = (LinearLayout) itemView.findViewById( R.id.market_list_id );

            CropName = (TextView)itemView.findViewById(R.id.cropName);
            CropPrice =(TextView)itemView.findViewById(R.id.cropPrice);
            CropDistrict =(TextView)itemView.findViewById(R.id.cropDistrict);
            CropDate = (TextView) itemView.findViewById(R.id.cropDate);
            edit =(ImageView) itemView.findViewById(R.id.edit);
            del = (ImageView) itemView.findViewById(R.id.delete);

        }
    }

    private void  Update(){

        ProgressDialog loadingBar = null;
        EditText name = (EditText) dialog.findViewById( R.id.Update_CropName );
        EditText price = (EditText) dialog.findViewById( R.id.Update_CropPrice );
        EditText dis = (EditText) dialog.findViewById( R.id.Update_CropDistrict );
        EditText date = (EditText) dialog.findViewById( R.id.Update_current_date );





        }

}
