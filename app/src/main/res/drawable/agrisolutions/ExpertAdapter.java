package com.example.agrisolutions;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExpertAdapter  extends   RecyclerView.Adapter<ExpertAdapter.MyViewHolder> {
    Context context;
    ArrayList<ExpertDetailsViewModel> posts;
        String id;

public ExpertAdapter(Context c, ArrayList<ExpertDetailsViewModel> p) {
        context = c;
        posts = p;
        }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.expert_show_deatials, parent, false);
        return new MyViewHolder(view);
    }




    @NonNull
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        holder.name.setText(posts.get(position).getName());
        holder.email.setText( posts.get(position).getEmail());
        holder.education.setText( posts.get(position).getEducation());
        holder.contact.setText( posts.get(position).getContact());
        holder.experties.setText( posts.get(position).getExperties());


    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, email, education, contact, experties;
        private CircleImageView profile_image;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.profile_expert_show);
            name = itemView.findViewById(R.id.show_expert_name);
            email = itemView.findViewById(R.id.show_expert_email);
            education = itemView.findViewById(R.id.show_expert_edu);
            contact = itemView.findViewById(R.id.show_expert_mobile);
            experties = itemView.findViewById(R.id.show_expert_experties);


        }
    }
}
