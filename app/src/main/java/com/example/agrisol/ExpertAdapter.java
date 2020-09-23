package com.example.agrisol;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public  class ExpertAdapter extends RecyclerView.Adapter<ExpertAdapter.ViewHolder>{

    private static final String Tag="RecyclerView";

        private ArrayList<Experts> expert;
        protected Context context;
        public ExpertAdapter(Context context, ArrayList<Experts> expert) {
            this.expert = expert;
            this.context = context;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expert_list, parent, false);
          //  viewHolder = new ExpertViewHolder(expert);
            return new ViewHolder(layoutView) ;


        }

        @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


            Picasso.get().load(expert.get(position).getProfile()).placeholder(R.mipmap.header_profile).into(holder.viewprofile);holder.expert_fullname.setText(expert.get(position).getExpert_Fullname());
            holder.expert_email.setText(expert.get(position).getExpert_Email());
            holder.expert_mobile.setText(expert.get(position).getExpert_Contact());
            holder.expert_qualification.setText(expert.get(position).getExpert_Qualification());
            holder.experties.setText(expert.get(position).getExpert_Experties());
            holder.expert_city.setText(expert.get(position).getExpert_City());
            holder.expert_pro.setText(expert.get(position).getExpert_Province());
            holder.expert_country.setText(expert.get(position).getExpert_Country());

    }


        @Override
        public int getItemCount() {
            return this.expert.size();
        }


      public static class ViewHolder extends  RecyclerView.ViewHolder{

            CircleImageView viewprofile;
            TextView expert_fullname,expert_email,expert_mobile,expert_qualification,experties,expert_city,expert_pro,expert_country;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                viewprofile = itemView.findViewById(R.id.View_ExpertProfile);
                expert_fullname = itemView.findViewById(R.id.View_fullname);
                expert_email = itemView.findViewById(R.id.View_email);
                expert_mobile = itemView.findViewById(R.id.View_mobile);
                expert_qualification = itemView.findViewById(R.id.View_qualification);
                experties = itemView.findViewById(R.id.View_expertise);
                expert_city = itemView.findViewById(R.id.View_city);
                expert_pro = itemView.findViewById(R.id.View_pro);
                expert_country = itemView.findViewById(R.id.View_country);
            }
        }
}
