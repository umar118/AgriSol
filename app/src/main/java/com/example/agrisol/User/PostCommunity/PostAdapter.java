package com.example.agrisol.User.PostCommunity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrisol.R;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context context;
    ArrayList<Post> posts;

    public PostAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate( R.layout.show_post, parent, false);
        return new PostAdapter.ViewHolder(layoutView) ;

    }

    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        holder.tile.setText( posts.get( position ).getTitle() );
      //  Picasso.get().load(posts.get(position).getImageUrl()).placeholder(R.mipmap.header_profile).into(holder.image);
      //  holder.desc.setText( posts.get( position ).getDesc() );
      //  holder.id.setText( posts.get( position ).getUsername() );

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tile,desc,id;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super( itemView );

           tile = itemView.findViewById( R.id.post_title_txtview );
            image = itemView.findViewById( R.id.post_image );
            desc = itemView.findViewById( R.id.post_desc_txtview);
            id = itemView.findViewById( R.id.post_user);


        }
    }

}
