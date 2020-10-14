package com.example.agrisol.Market;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrisol.R;

import java.util.ArrayList;

public class MarketAdapter extends RecyclerView.Adapter<MarketAdapter.ViewHolder> {

    Context context;
    ArrayList<Market> model;

    public MarketAdapter(Context context, ArrayList<Market> model) {
        this.context = context;
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.market_row, parent, false);
        return new MarketAdapter.ViewHolder(layoutView) ;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.CropName.setText(model.get(position).getName());
        holder.CropPrice.setText(model.get(position).getPrice());
        holder.CropDistrict.setText(model.get(position).getDistrict());
        holder.CropDate.setText(model.get(position).getDate());

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView CropName,CropPrice,CropDistrict,CropDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            CropName =itemView.findViewById(R.id.show_cropName);
            CropPrice =itemView.findViewById(R.id.show_cropPrice);
            CropDistrict =itemView.findViewById(R.id.show_cropDistrict);
            CropDate =itemView.findViewById(R.id.show_cropDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
