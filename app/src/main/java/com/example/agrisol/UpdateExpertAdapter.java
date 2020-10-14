package com.example.agrisol;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrisol.Admin.AddExpertDetials;
import com.example.agrisol.UpdateExpert.UpdateExpert;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;

public class UpdateExpertAdapter extends RecyclerView.Adapter<UpdateExpertAdapter.ViewHolder>{

    private static final String Tag="RecyclerView";

    private ArrayList<UpdateExpert> expert;
    protected Context context;
    public UpdateExpertAdapter(Context context, ArrayList<UpdateExpert> expert) {
        this.expert = expert;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.update_expertlist, parent, false);
        //  viewHolder = new ExpertViewHolder(expert);
        return new ViewHolder(layoutView) ;


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {


        Picasso.get().load(expert.get(position).getProfile()).placeholder(R.mipmap.header_profile).into(holder.viewprofile);holder.expert_fullname.setText(expert.get(position).getExpert_Fullname());
        holder.expert_email.setText(expert.get(position).getExpert_Email());
        holder.expert_mobile.setText(expert.get(position).getExpert_Contact());
        holder.expert_qualification.setText(expert.get(position).getExpert_Qualification());
        holder.experties.setText(expert.get(position).getExpert_Experties());
        holder.expert_city.setText(expert.get(position).getExpert_City());
        holder.expert_pro.setText(expert.get(position).getExpert_Province());
        holder.expert_country.setText(expert.get(position).getExpert_Country());
        holder.edit.setText(expert.get(position).getUpdate());
        holder.delete.setText(expert.get(position).getDelete());

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id;
                id=expert.get(position).getExpert_Email();
                Intent editIntent =new Intent(context, AddExpertDetials.class);
                editIntent.putExtra("UpdateExpert",id);
                context.startActivity(editIntent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return this.expert.size();
    }


    public  class ViewHolder extends  RecyclerView.ViewHolder{

        CircleImageView viewprofile;
        Button edit,delete;
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
            edit =itemView.findViewById(R.id.edit_expert);
            delete =itemView.findViewById(R.id.expert_delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String email =expert.get(getAdapterPosition()).getExpert_Email();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                    Query applesQuery = ref.orderByChild("Expert").equalTo(email);
                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                appleSnapshot.getRef().removeValue();
                            }
                        }
                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.e(TAG, "onCancelled", databaseError.toException());
                        }
                    });
                }
            });
        }
    }

}
