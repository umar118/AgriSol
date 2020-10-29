package com.example.agrisol.User.PostCommunity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agrisol.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class Community extends Fragment {
    private RecyclerView recyclerView;

    private DatabaseReference UserRef,PostRef,LikesRef;

    private FirebaseAuth mAuth;
    private Query query;
    String currentUserID;
    Boolean LikeChecker = false;
    private Context context;
    private CardView ask;
    private String platitude,plongtitude;
    private ArrayList<Post> postList;
    private PostAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate( R.layout.community_fragment, container, false );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated( savedInstanceState );

        ask = getView().findViewById( R.id.Ask );
        ask.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getContext(), PsotActivity.class ) );
            }
        } );

        postList = new ArrayList<>(  );

        recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        UserRef = FirebaseDatabase.getInstance().getReference().child("Users").child( "User" );
        PostRef = FirebaseDatabase.getInstance().getReference().child("Post");
        LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");


        query = FirebaseDatabase.getInstance().getReference().child("Posts");
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

AllUsersPosts();
    }



    private void AllUsersPosts(){

        Query SortPostsInDesendingOrder = PostRef.orderByChild("counter");
        FirebaseRecyclerOptions<Post> options = new FirebaseRecyclerOptions.Builder<Post>().setQuery(SortPostsInDesendingOrder, Post.class).build();

        FirebaseRecyclerAdapter adapter =new FirebaseRecyclerAdapter<Post,PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostViewHolder holder, int position, @NonNull Post model) {
                final String PostKey = getRef(position).getKey();
                holder.setFullname(model.getFullname());
                holder.setTitle( model.getTitle() );
                holder.setDescription(model.getDescription());
                holder.setProfileImage(getContext(), model.getProfileimage());
                holder.setPostImage(getContext(), model.getPostimage());
                holder.setDate(model.getDate());
                holder.setTime(model.getTime());
                platitude=model.getLatitude();
                plongtitude=model.getLongitude();
                Log.d("getlan", "onBindViewHolder: "+ model.getLatitude()+""+model.getLongitude());

                holder.setLikeButtonStatus(PostKey);

                holder.CommentPostButton.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent clickPostIntent= new Intent( getContext(),CommentsActivity.class );
                        clickPostIntent.putExtra("PostKey", PostKey);
                        startActivity( clickPostIntent );
                    }
                } );

            }
            @NonNull
            @Override
            public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.show_post, parent, false);
                return new PostViewHolder( view );
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

 public  static  class PostViewHolder extends  RecyclerView.ViewHolder{
     ImageButton LikePostButton, CommentPostButton,MapPostButton;
     TextView DisplayNoOfLikes;
     int countLikes;
     String currentUserId;
     DatabaseReference LikesRaf;
        View mView;
     public PostViewHolder(@NonNull View itemView) {
         super( itemView );
         mView=itemView;

         LikePostButton = mView.findViewById(R.id.like_button);
         CommentPostButton = mView.findViewById(R.id.comment_button);
         DisplayNoOfLikes = mView.findViewById(R.id.display_no_of_likes);

         LikesRaf = FirebaseDatabase.getInstance().getReference().child("Likes");
         currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();



     }

     public void setLikeButtonStatus(final String PostKey) {
         LikesRaf.addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 if (dataSnapshot.child(PostKey).hasChild(currentUserId)) {
                     countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
                     LikePostButton.setImageResource(R.drawable.like);
                     DisplayNoOfLikes.setText(Integer.toString(countLikes) + (" Likes"));
                 } else {
                     countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
                     LikePostButton.setImageResource(R.drawable.dislike);
                     DisplayNoOfLikes.setText(Integer.toString(countLikes) + (" Likes"));

                 }

             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {

             }
         });
     }


     public void setFullname(String fullname) {
         TextView username = (TextView) mView.findViewById(R.id.post_user);
         username.setText(fullname);
     }

     public void setProfileImage(Context ctx, String profileimage) {
         CircleImageView image = (CircleImageView) mView.findViewById(R.id.post_profile_image);
         Picasso.get().load(profileimage).into(image);

     }

     public void setTitle(String title){
         TextView Title = (TextView) mView.findViewById(R.id.post_title_txtview);
         Title.setText(title);
     }

     public void setTime(String time) {
         TextView PostTime = (TextView) mView.findViewById(R.id.post_time);
         PostTime.setText("    " + time);
     }

     public void setDate(String date) {
         TextView postDate = (TextView) mView.findViewById(R.id.post_date);
         postDate.setText("     " + date);
     }

     public void setDescription(String description) {
         TextView postDescription = (TextView) mView.findViewById(R.id.post_desc_txtview);
         postDescription.setText(description);
     }

     public void setPostImage(Context ctx1, String postImage) {
         ImageView postImages = (ImageView) mView.findViewById(R.id.post_image);
         Picasso.get().load(postImage).into(postImages);
     }
 }

}
