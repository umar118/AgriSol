package com.example.agrisol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MarketAdapter extends  RecyclerView.Adapter<MarketAdapter.ViewHolder> {

    ArrayList<Market> marketlist;
    Context context;

    public MarketAdapter(Context context,ArrayList<Market> marketlist){
        this.marketlist=marketlist;
        this.context=context;
    }


    @NonNull
    @Override
    public MarketAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.price_list, parent, false);
        //  viewHolder = new ExpertViewHolder(expert);
        return new MarketAdapter.ViewHolder(layoutView) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MarketAdapter.ViewHolder holder, int position) {

        holder.cropName.setText(marketlist.get(position).getCropName());
        holder.cropPrice.setText(marketlist.get(position).getCropPrice());
        holder.district.setText(marketlist.get(position).getDistrict());
    }

    @Override
    public int getItemCount() {
        return  marketlist.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView cropName,cropPrice,district;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cropName=itemView.findViewById(R.id.view_crop_name);
            cropPrice=itemView.findViewById(R.id.view_crop_price);
            district=itemView.findViewById(R.id.view_district);
        }
    }
}
