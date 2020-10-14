package com.example.agrisol.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.agrisol.Login.AboutUs;
import com.example.agrisol.Login.DeleteAccountDailogFragment;
import com.example.agrisol.Expert.ExpertDetials;
import com.example.agrisol.Login.Login;
import com.example.agrisol.Market.MarketPriceList;
import com.example.agrisol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.View;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

   private DrawerLayout drawerLayout;
   private NavigationView  navigationView;
    private DatabaseReference UsersRef;
    private CircleImageView ProfileImage;
    private TextView ProfileName;
    private StorageReference UserProfileImageRef;
    private FirebaseAuth mAuth;
    View profileView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_dashboard);






        mAuth = FirebaseAuth.getInstance();
     //  String currentUserID = mAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child("User");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child("User").child(user.getUid());
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("User Profile Images");
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {


                    if (dataSnapshot.hasChild("userprofileimage")) {
                        //String image = "https://firebasestorage.googleapis.com/v0/b/poster-44926.appspot.com/o/Profile%20Images%2FjIR4L7pSWphSsBlBT8xu52FXI6L2.jpg?alt=media&token=86f09465-0562-4a00-ae15-1b5d9d94727b";
                        String userprofileimage = dataSnapshot.child("userprofileimage").getValue().toString();
                        Picasso.get().load(userprofileimage).placeholder(R.drawable.profile).into(ProfileImage);

                        //   Picasso.with(MainActivity.this).load(image).placeholder(R.drawable.profile_image).into(NavProfileImage);

                    }
                    if (dataSnapshot.hasChild("user_FullName")) {
                        String fullname = dataSnapshot.child("user_FullName").getValue().toString();
                        //  Toast.makeText(MainActivity.this, "Name: "+fullname, Toast.LENGTH_LONG).show();
                        ProfileName.setText(fullname);
                    }
                    else {
                        Toast.makeText(UserDashboard.this, "Profile name do not exists...", Toast.LENGTH_SHORT).show();
                    }

                   // String image = dataSnapshot.child("userprofileimage").getValue().toString();
                   // String user_FullName = dataSnapshot.child("user_FullName").getValue().toString();
                   // ProfileName.setText(user_FullName);
                   // Picasso.get().load(image).placeholder(R.drawable.profile).into(ProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("AgriSolutions");

        loadFragment(new UserHome());

        drawerLayout =findViewById(R.id.drawerlayout1);
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


        navigationView =(NavigationView) findViewById(R.id.navigation_view1);
        navigationView.setNavigationItemSelectedListener(this);

        ProfileImage =(CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.header_Profile);
        ProfileName=navigationView.getHeaderView(0).findViewById(R.id.header_name);


        BottomNavigationView navigation = findViewById(R.id.navigation1);
        navigation.setOnNavigationItemSelectedListener(bottom);
    }


    private BottomNavigationView.OnNavigationItemSelectedListener bottom =new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment fragment = null;

            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    fragment = new UserHome();
                    break;

                case R.id.navigation_expert:
                    fragment = new ExpertDetials();
                    break;

                case R.id.navigation_market:
                    fragment = new MarketPriceList();
                    break;

                case R.id.navigation_profile:
                    fragment = new UserProfile();
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + menuItem.getItemId());
            }

            return loadFragment(fragment);

        }
    };

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container1, fragment)
                    .commit();
            return true;
        }
        return false;
    }




        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            switch (menuItem.getItemId()){
                case R.id.nav_update:
                   startActivity(new Intent(getApplicationContext(), UpdateUserProfile.class));
                   finish();
                    break;
                case  R.id.nav_signout:

                    mAuth.signOut();
                    Toast.makeText(getApplicationContext(),"Sign out Successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    break;

                case R.id.nav_delete:

                    final   FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential authCredential = EmailAuthProvider.getCredential("user@example.com", "password1234");
                    FragmentManager manager =getSupportFragmentManager();

                   DeleteAccountDailogFragment myDialogFragment =new DeleteAccountDailogFragment();
                    myDialogFragment.show(manager,"DeleteAccountDialogFragment");
                    firebaseUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firebaseUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(UserDashboard.this, "User account deleted", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplication(),Login.class));
                                    }
                                }
                            });
                        }
                    });


                    break;
                case R.id.nav_share:
                    Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_send:
                    Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
                    break;
                case R.id.menu_Setting:
                   startActivity(new Intent(getApplicationContext(), Setting.class));
                   finish();
                    break;

                case R.id.menu_about_us:
                    startActivity(new Intent(getApplicationContext(), AboutUs.class));
                    finish();
                    break;

            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;

        }

}

