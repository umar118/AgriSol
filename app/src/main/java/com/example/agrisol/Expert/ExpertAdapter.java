package com.example.agrisol.Expert;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrisol.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public  class ExpertAdapter extends RecyclerView.Adapter<ExpertAdapter.ViewHolder>{

    private static final String Tag="RecyclerView";


         ArrayList<Experts> expert;
         Context context;
          Dialog dialog;
        public ExpertAdapter(Context context, ArrayList<Experts> expert) {
            this.expert = expert;
            this.context = context;
        }
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.expert_list, parent, false);
          //  viewHolder = new ExpertViewHolder(expert);
            final ViewHolder viewHolder =new  ViewHolder(layoutView);
            viewHolder.expert_list.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog =new Dialog( context );
                    dialog.setContentView( R.layout.expert_dialog );
                    // dialog.getWindow().setBackgroundDrawable( new ColorDrawable( Color.TRANSPARENT ) );

                    final CircleImageView     profile = (CircleImageView) dialog.findViewById(R.id.dialog_profile);
                    final TextView     name = (TextView) dialog.findViewById(R.id.dialog_name);
                    final TextView     email =(TextView) dialog.findViewById(R.id.dialog_email);
                    final TextView     mobile =(TextView) dialog.findViewById(R.id.dialog_mobile);
                    final TextView     qualification =(TextView) dialog.findViewById(R.id.dialog_qualification);
                    final TextView     expertise =(TextView) dialog.findViewById(R.id.dialog_expertise);
                    final TextView     city =(TextView) dialog.findViewById(R.id.dialog_city);
                    final TextView     province = (TextView)dialog.findViewById(R.id.dialog_province);
                    final TextView     country =(TextView) dialog.findViewById(R.id.dialog_country);

                    Picasso.get().load(expert.get( viewHolder.getAdapterPosition()).getProfile()).placeholder(R.mipmap.header_profile).into(profile);
                    name.setText(expert.get(viewHolder.getAdapterPosition()).getExpert_Fullname());
                    email.setText(expert.get( viewHolder.getAdapterPosition()).getExpert_Email());
                    mobile.setText(expert.get(viewHolder.getAdapterPosition()).getExpert_Contact());
                    qualification.setText(expert.get(viewHolder.getAdapterPosition()).getExpert_Qualification());
                    expertise.setText(expert.get(viewHolder.getAdapterPosition()).getExpert_Experties());
                    city.setText(expert.get(viewHolder.getAdapterPosition()).getExpert_City());
                    province.setText(expert.get(viewHolder.getAdapterPosition()).getExpert_Province());
                    country.setText(expert.get(viewHolder.getAdapterPosition()).getExpert_Country());

                    Toast.makeText( context,""+String.valueOf( viewHolder.getAdapterPosition() ), Toast.LENGTH_SHORT ).show();
                    dialog.show();

                }
            } );
            return viewHolder;


        }

        @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {


            Picasso.get().load(expert.get(position).getProfile()).placeholder(R.mipmap.header_profile).into(holder.viewprofile);
            holder.expert_fullname.setText(expert.get(position).getExpert_Fullname());
            holder.expert_mobile.setText(expert.get(position).getExpert_Contact());
            holder.experties.setText(expert.get(position).getExpert_Experties());



    }


        @Override
        public int getItemCount() {
            return this.expert.size();
        }


      public static class ViewHolder extends  RecyclerView.ViewHolder{

            LinearLayout expert_list;
            CircleImageView viewprofile;
            TextView expert_fullname,expert_email,expert_mobile,expert_qualification,experties,expert_city,expert_pro,expert_country;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                expert_list =(LinearLayout) itemView.findViewById( R.id.expert_list_id );
                viewprofile = itemView.findViewById(R.id.View_ExpertProfile);
                expert_fullname = itemView.findViewById(R.id.View_fullname);
                expert_mobile = itemView.findViewById(R.id.View_mobile);
                experties = itemView.findViewById(R.id.View_expertise);




            }
        }
}
